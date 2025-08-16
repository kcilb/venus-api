package com.neptunesoftware.venusApis.Services;

import com.neptunesoftware.supernova.ws.common.XAPIException;
import com.neptunesoftware.supernova.ws.common.XAPIRequestBaseObject;
import com.neptunesoftware.supernova.ws.server.account.data.AccountBalanceOutputData;
import com.neptunesoftware.supernova.ws.server.account.data.AccountBalanceRequest;
import com.neptunesoftware.supernova.ws.server.transaction.data.GLTransferOutputData;
import com.neptunesoftware.supernova.ws.server.transaction.data.GLTransferRequest;
import com.neptunesoftware.supernova.ws.server.transaction.data.TxnResponseOutputData;
import com.neptunesoftware.supernova.ws.server.txnprocess.data.XAPIBaseTxnRequestData;
import com.neptunesoftware.venusApis.Beans.AppProps;
import com.neptunesoftware.venusApis.Beans.ItemCacheService;
import com.neptunesoftware.venusApis.Models.AlertCharge;
import com.neptunesoftware.venusApis.Models.AlertRequest;
import com.neptunesoftware.venusApis.Models.ApiResponse;
import com.neptunesoftware.venusApis.Repository.AlertsDao;
import com.neptunesoftware.venusApis.Util.Logging;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.neptunesoftware.supernova.ws.server.account.AccountWebServiceEndPointPort;
import com.neptunesoftware.supernova.ws.server.account.AccountWebServiceStub;
import com.neptunesoftware.supernova.ws.server.transaction.TransactionsWebServiceEndPointPort;
import com.neptunesoftware.supernova.ws.server.transaction.TransactionsWebServiceStub;
import com.neptunesoftware.supernova.ws.server.txnprocess.TxnProcessWebServiceEndPointPort;
import com.neptunesoftware.supernova.ws.server.txnprocess.TxnProcessWebServiceStub;


@Service
public class ChargeService {

    private final AppProps appProps;
    private final ItemCacheService cacheService;
    private final AlertsDao alertsDao;
    private static final int PAGE_SIZE = 100;
    private static final int MAX_RETRIES = 3;
    private static final int THREAD_POOL_SIZE = 4; // Match your API rate limits
    private static final int RETRY_DELAY_MS = 1000;

    private final SimpleDateFormat format = new SimpleDateFormat("MMM yyyy");

    // Thread-safe counters
    private final AtomicInteger processedRecords = new AtomicInteger(0);
    private final AtomicInteger posted = new AtomicInteger(0);
    private final AtomicInteger failed = new AtomicInteger(0);
    private final AtomicInteger lowFunds = new AtomicInteger(0);
    private final AtomicInteger syserr = new AtomicInteger(0);
    private final AtomicReference<BigDecimal> totalCharge = new AtomicReference<>(BigDecimal.ZERO);
    private static List<AlertCharge> sms_charges = Collections.emptyList();
    private int total;

    public ChargeService(AppProps appProps, ItemCacheService cacheService, AlertsDao alertsDao) {
        this.appProps = appProps;
        this.cacheService = cacheService;
        this.alertsDao = alertsDao;
        loadEndpointFunctions();
    }


    //generate pdf report and return to front-end
    public ApiResponse<String> processSMSCharges(String resultSetView, boolean isAutoRecoveryInitiated) {
        Logging.info("Method Entry: ChargePosting.run");
        long startTime = System.currentTimeMillis();
        try {
            // Initialize core connection
            loadCoreConnection();

            // Load charge configurations (thread-safe)
            loadCharges();

            // Get total count
            getTotalRecords(resultSetView);

            // Process all charges
            processAllCharges(resultSetView);

        } catch (Exception e) {
            Logging.error("Processing failed", e);
            Logging.info(e.getMessage(), e);
        } finally {
            // Log final results
            logResults(startTime, isAutoRecoveryInitiated);
        }
        return null;
    }

    private void processAllCharges(String resultSetView) throws SQLException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CompletionService<Void> completionService = new ExecutorCompletionService<>(executor);

        int pageNumber = 1;
        boolean hasMoreResults = true;

        while (hasMoreResults) {
            List<AlertRequest> batch = fetchBatch(pageNumber, resultSetView);
            if (batch.isEmpty()) {
                hasMoreResults = false;
                continue;
            }

            // Submit each record in batch for parallel processing
            for (AlertRequest chargeData : batch) {
                completionService.submit(() -> {
                    processSingleCharge(chargeData);
                    return null;
                });
            }

            // Wait for partial completion to control memory usage
            for (int i = 0; i < batch.size(); i++) {
                completionService.take();
            }

            pageNumber++;
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
    }


    private List<AlertRequest> fetchBatch(int pageNumber, String resultSetView) throws SQLException {

        List<AlertRequest> batch = Collections.emptyList();

        //sms alert currency id has to exist in the smsbank table
        // comes with the iso code/currency attached to the account
        List<AlertRequest> alertRequests = alertsDao.findPendingCharges(pageNumber, PAGE_SIZE, resultSetView);
        if (alertRequests.isEmpty())
            return Collections.emptyList();

        for (AlertRequest alertRequest : alertRequests) {
            // filter all charges attached to the currency
            sms_charges = sms_charges.stream().filter(f -> f.getSmsAlertCrncyId() == alertRequest.getSmsAlertCrncyId()).toList();
            AlertCharge charge = computeCharge(alertRequest.getSmsCount());
            batch.add(buildAlertRequest(alertRequest, charge));
        }
        return batch;
    }

    private AlertCharge computeCharge(int sms_count) {
        return sms_charges.stream()
                .filter(c -> sms_count >= c.getMinValue() &&
                        sms_count <= c.getMaxValue())
                .findFirst()
                .orElse(null);
    }

    private AlertRequest buildAlertRequest(AlertRequest alertRequest, AlertCharge charge) throws SQLException {
        return new AlertRequest(alertRequest.getAccount(), null, null,
                String.valueOf(System.currentTimeMillis()), alertRequest.getTxnCurrency(), "SMS", null,
                alertRequest.getAccount(), null,
                unmaskGLAccount(alertRequest.getGlPrefixCd(), appProps.bankChargeGl),
                unmaskGLAccount(alertRequest.getGlPrefixCd(), appProps.taxChargeGl),
                unmaskGLAccount(alertRequest.getGlPrefixCd(), appProps.vendorChargeGl), null, null,
                null, null, format.format(alertRequest.getLogDate()) + " Monthly SMS Charge",
                alertRequest.getAccount(), null, null, charge.getTotalCharge(), charge.getBankCharge(),
                charge.getExciseCharge(), charge.getVendorCharge(), 9L, alertRequest.getSmsCount(), false,
                false, false, alertRequest.getSmsCount(), alertRequest.getLogDate(),
                alertRequest.getSmsAlertCrncyId());
    }

    private void handleInsufficientFunds(AlertRequest chargeData) throws SQLException {
        try {
            alertsDao.updateSMSCount(chargeData.getAccount(), "Insufficient Funds On Account.",
                    "N", null, chargeData.getSmsCount(), chargeData.getLogDate());
        } catch (Exception e) {
            Logging.info("Error updating insufficient balance account SMS count");
            Logging.info(e.getMessage(), e);
        }
    }

    private void processSingleCharge(AlertRequest chargeData) {
        try {

            Logging.info(">>>>>>>>>>>>>>>>> ACCOUNT_CHARGE_PROCESSING <<<<<<<<<<<<<<<<<<<<<<<");
            Logging
                    .info("Balance Check  for " + chargeData.getAccount());
            BigDecimal balance = queryDepositAccountBalance(chargeData);

            if (balance == null) {
                syserr.incrementAndGet();
                processedRecords.incrementAndGet();
                return;
            }

            Logging.info(">>>>>>>>>>>>>>>>> ACCOUNT_CHARGE_POSTING <<<<<<<<<<<<<<<<<<<<<<<");
            Logging
                    .info("Handling Charge Posting for " + chargeData.getAccount() + " with bal " + balance);

            if (balance.compareTo(chargeData.getChargeAmount()) <= 0) {
                handleInsufficientFunds(chargeData);
                lowFunds.incrementAndGet();
                processedRecords.incrementAndGet();
                return;
            }

            boolean success = attemptChargePostingWithRetry(chargeData);
            if (success) {
                posted.incrementAndGet();
                totalCharge.updateAndGet(current ->
                        current.add(chargeData.getBankCharge())
                                .add(chargeData.getTaxCharge())
                                .add(chargeData.getVendorCharge()));
            } else {
                failed.incrementAndGet();
            }
            processedRecords.incrementAndGet();
        } catch (SQLException e) {
            Logging.error("Processing failed for " + chargeData.getAccount(), e);
            failed.incrementAndGet();
        }
    }

    public BigDecimal queryDepositAccountBalance(AlertRequest tXRequest) {
        try {

            if (accountEndPoint == null)
                throw new RuntimeException("No connection established to core API");

            AccountBalanceRequest accountBalanceRequest = new AccountBalanceRequest();
            accountBalanceRequest = (AccountBalanceRequest) getBaseRequest(accountBalanceRequest, tXRequest);
            accountBalanceRequest.setAccountNumber(tXRequest.getChargeAcct());


            AccountBalanceOutputData data = accountEndPoint.findAccountBalance(accountBalanceRequest);
            return data.getAvailableBalance() == null ? BigDecimal.ZERO : data.getAvailableBalance();
        } catch (Exception e) {
            Logging.error(e);
            if (e instanceof XAPIException && ((XAPIException) e).getErrorCodes().length > 0) {
                String errorCode = ((XAPIException) e).getErrorCodes()[0];
                tXRequest.setErrorcode(errorCode);
                tXRequest.setReason(getErrorDesc(errorCode));
            }
            return BigDecimal.ZERO;
        } finally {
            Logging.info("Processed Balance Enquiry " + tXRequest.getAccount());

        }
    }


    private boolean attemptChargePostingWithRetry(AlertRequest chargeData) throws SQLException {
        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            try {
                boolean bankPosted = postCharge("Bank", chargeData);
                boolean taxPosted = bankPosted && postCharge("Tax", chargeData);
                boolean vendorPosted = taxPosted && postCharge("Vendor", chargeData);

                if (vendorPosted) {
                    updateChargeStatus(chargeData);
                    return true;
                } else {
                    logFailedCharge(chargeData);
                    return false;
                }
            } catch (Exception e) {
                attempts++;
                Logging.warn(String.format(
                        "Attempt %d failed for %s: %s",
                        attempts, chargeData.getAccount(), e.getMessage()));

                if (attempts < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS * attempts);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        return false;
    }

    private boolean postCharge(String benefactor, AlertRequest irequest) {

        if ("Bank".equalsIgnoreCase(benefactor)) {
            if (irequest.getBankCharge().compareTo(BigDecimal.ZERO) <= 0) {
                Logging.info("Skipping, no bank charge amount was configured");
                irequest.setBankChargePosted(false);
                return true;
            }
            if (irequest.getBankChargeGL() == null || irequest.getBankChargeGL() == "") {
                Logging.info("Skipping. No Bank Charge GL Configured");
                irequest.setBankChargePosted(false);
                return true;
            }
            irequest.setAccount(irequest.getChargeAcct());
            irequest.setContraAccount(irequest.getBankChargeGL());
            irequest.setTxnAmount(irequest.getBankCharge().add(irequest.getVendorCharge()));
            irequest.setTxnDesc(irequest.getChargeDesc());
            Object response = postDepositToGLTransfer(irequest);
            if (response instanceof TxnResponseOutputData) {
                irequest.setBankChargePosted("00".equals(((TxnResponseOutputData) response).getResponseCode()));
            } else if (response instanceof XAPIException) {
                if (((XAPIException) response).getErrorCodes().length > 0) {
                    irequest.setReason(((XAPIException) response).getErrorCodes()[0]);
                }
                Logging.info("Error processing bank charge " + irequest.getReason());
            }
            return irequest.isBankChargePosted();
        } else if ("Vendor".equalsIgnoreCase(benefactor)) {
            if (BigDecimal.ZERO.compareTo(irequest.getVendorCharge()) > 0) {
                Logging.info("Skipping. No vendor charge amount was configured");
                irequest.setVendorChargePosted(false);
                return true;
            }
            if (irequest.getVendorChargeGL() == null || irequest.getVendorChargeGL() == "") {
                Logging.info("Skipping. No vendor charge ledger was found");
                irequest.setVendorChargePosted(false);
                return true;
            }
            irequest.setAccount(irequest.isBankChargePosted() ? irequest.getBankChargeGL() : irequest.getChargeAcct());
            irequest.setContraAccount(irequest.getVendorChargeGL());
            irequest.setTxnAmount(irequest.getVendorCharge());
            irequest.setTxnDesc(irequest.getChargeDesc().concat(" ~ Vendor"));
            Object response = irequest.isBankChargePosted() ? postGLToGLTransfer(irequest)
                    : postDepositToGLTransfer(irequest);
            if (response instanceof GLTransferOutputData) {
                irequest.setVendorChargePosted("00".equals(((GLTransferOutputData) response).getResponseCode()));
            } else if (response instanceof TxnResponseOutputData) {
                irequest.setVendorChargePosted("00".equals(((TxnResponseOutputData) response).getResponseCode()));
            } else if (response instanceof XAPIException) {
                if (((XAPIException) response).getErrorCodes().length > 0) {
                    irequest.setReason(((XAPIException) response).getErrorCodes()[0]);
                }
                Logging.info("Error processing vendor charge " + irequest.getReason());
            }
            return irequest.isVendorChargePosted();
        } else if ("Tax".equalsIgnoreCase(benefactor)) {
            if (irequest.getTaxCharge().compareTo(BigDecimal.ZERO) <= 0) {
                Logging.info("Skipping Tax Charge Processing. No Tax Charge Amount Configured");
                irequest.setTaxChargePosted(false);
                return true;
            }
            if (irequest.getTaxChargeGL() == null || irequest.getTaxChargeGL() == "") {
                Logging.info("Skipping Tax Charge Processing. No Tax Charge GL Configured");
                irequest.setTaxChargePosted(false);
                return true;
            }
            irequest.setAccount(irequest.getChargeAcct());
            irequest.setContraAccount(irequest.getTaxChargeGL());
            irequest.setTxnAmount(irequest.getTaxCharge());
            irequest.setTxnDesc(irequest.getChargeDesc().concat(" ~ Excise duty"));
            Object response = postDepositToGLTransfer(irequest);
            if (response instanceof TxnResponseOutputData) {
                irequest.setTaxChargePosted("00".equals(((TxnResponseOutputData) response).getResponseCode()));
            } else if (response instanceof XAPIException) {
                if (((XAPIException) response).getErrorCodes().length > 0) {
                    irequest.setReason(((XAPIException) response).getErrorCodes()[0]);
                }
                Logging.info("Error processing Tax charge " + irequest.getReason());
            }
            return irequest.isTaxChargePosted();
        }

        Logging.info("Method Exit : AlertCharger.postCharge");

        return true;
    }


    public XAPIRequestBaseObject getBaseRequest(XAPIRequestBaseObject requestData, AlertRequest tXRequest) {
        requestData.setOriginatorUserId(-99L);
        requestData.setUserId(-99L);
        requestData.setUserLoginId("SYSTEM");
        requestData.setChannelId(tXRequest.getChannelId());
        requestData.setChannelCode(tXRequest.getChannelCode());
        requestData.setCardNumber(tXRequest.getCardNumber());
        requestData.setTransmissionTime(System.currentTimeMillis());
        requestData.setTerminalNumber(tXRequest.getChannelCode());
        return requestData;
    }

    public Object postGLToGLTransfer(AlertRequest tXRequest) {

        Logging.info("Processing GL2GL Transfer " + tXRequest.getTxnDesc() + " "
                + tXRequest.getAccount() + " --> " + tXRequest.getContraAccount());

        try {
            GLTransferRequest glTransferRequest = new GLTransferRequest();
            glTransferRequest = (GLTransferRequest) getBaseRequest(glTransferRequest, tXRequest);
            glTransferRequest.setFromGLAccountNumber(tXRequest.getAccount());
            glTransferRequest.setToGLAccountNumber(tXRequest.getContraAccount());
            glTransferRequest.setTransactionAmount(tXRequest.getTxnAmount());
            glTransferRequest.setTransactionCurrencyCode(tXRequest.getTxnCurrency());
            glTransferRequest.setTxnDescription(tXRequest.getTxnDesc());
            glTransferRequest.setReference(tXRequest.getTxnReference());
            return transactionsEndPoint.postGLtoGLXfer(glTransferRequest);
        } catch (Exception e) {
            Logging.error(e);
            if (e instanceof XAPIException && ((XAPIException) e).getErrorCodes().length > 0) {
                String errorCode = ((XAPIException) e).getErrorCodes()[0];
                tXRequest.setErrorcode(errorCode);
                tXRequest.setReason(getErrorDesc(errorCode));
            }
            return e;

        } finally {
            Logging.info("Processed GL2GL Transfer " + tXRequest.getTxnDesc() + " "
                    + tXRequest.getAccount() + " --> " + tXRequest.getContraAccount());
        }

    }

    public Object postDepositToGLTransfer(AlertRequest tXRequest) {
        try {
            XAPIBaseTxnRequestData requestData = new XAPIBaseTxnRequestData();
            requestData = (XAPIBaseTxnRequestData) getBaseRequest(requestData, tXRequest);
            requestData.setAcctNo(tXRequest.getAccount());
            requestData.setContraAcctNo(tXRequest.getContraAccount());
            requestData.setTxnDescription(tXRequest.getTxnDesc());
            requestData.setTxnAmount(tXRequest.getTxnAmount());
            requestData.setTransmissionTime(System.currentTimeMillis());
            requestData.setReference(tXRequest.getTxnReference());
            requestData.setTxnReference(tXRequest.getTxnReference());
            requestData.setTxnCurrencyCode(tXRequest.getTxnCurrency());
            return txnWebEndPoint.postDepositToGLAccountTransfer(requestData);
        } catch (Exception e) {
            Logging.error(e);

            if (e instanceof XAPIException && ((XAPIException) e).getErrorCodes().length > 0) {
                String errorCode = ((XAPIException) e).getErrorCodes()[0];
                tXRequest.setErrorcode(errorCode);
                tXRequest.setReason(getErrorDesc(errorCode));
            }
            return e;
        }
    }


    private void updateChargeStatus(AlertRequest chargeData) {
        try {
            alertsDao.updateSMSCount(chargeData.getAccount(), "Success", "C", chargeData.getChargeAmount(),
                    chargeData.getSmsCount(), new java.sql.Date(chargeData.getLogDate().getTime()));
        } catch (Exception ex) {
            Logging.info("Failed to update charge status");
            throw ex;
        }
    }

    private void logFailedCharge(AlertRequest chargeData) {

        try {
            alertsDao.logFailedSplit(chargeData);
        } catch (Exception ex) {
            Logging.info("Failed to log failed split");
            Logging.error(ex);
            throw ex;
        }
    }


    public String unmaskGLAccount(String glPrefix, String glAccount) {
        if (glPrefix != null && glAccount.contains("***")) {
            glAccount = glPrefix + glAccount.substring(glAccount.indexOf("***") + 3);
        }
        return glAccount;
    }

    private void loadCharges() {
        sms_charges = cacheService.getCachedItem().chargesList;
    }

    private void getTotalRecords(String resultSetView) {
        total = alertsDao.getTotalRecords(resultSetView);
    }

    public static Map<String, String> endpointFunctions = new HashMap<String, String>();
    public static String rubiApi = null;
    public AccountWebServiceStub accountEndPoint;
    private TxnProcessWebServiceStub txnWebEndPoint;
    private TransactionsWebServiceStub transactionsEndPoint;

    private void loadEndpointFunctions() {
        readXapiCodes();
        // rubiApi = appProps.getRubiApi();
        endpointFunctions.put("account-web-service", "AccountWebServiceEndPointPort?wsdl");
        endpointFunctions.put("transaction-web-service", "TransactionsWebServiceEndPointPort?wsdl");
        endpointFunctions.put("txnprocess-web-service", "TxnProcessWebServiceEndPointPort?wsdl");
    }


    //call this when running the charges job
    public boolean loadCoreConnection() {
        try {
            Logging.info("Method Entry : AlertCharger.initCoreConnection");

            accountEndPoint = new AccountWebServiceEndPointPort(
                    new URL("http://10.0.1.13:9003/supernovaws/" + endpointFunctions.get("account-web-service")))
                    .getAccountWebServiceStubPort();

            transactionsEndPoint = new TransactionsWebServiceEndPointPort(
                    new URL("http://10.0.1.13:9003/supernovaws/" + endpointFunctions.get("transaction-web-service")))
                    .getTransactionsWebServiceStubPort();

            txnWebEndPoint = new TxnProcessWebServiceEndPointPort(
                    new URL("http://10.0.1.13:9003/supernovaws/" + endpointFunctions.get("txnprocess-web-service")))
                    .getTxnProcessWebServiceStubPort();

            Logging.info("Method Exit : AlertCharger.initCoreConnection");
            return true;
        } catch (Exception e) {
            Logging.error(e);
        }
        return false;
    }

    private static final Properties xapiCodes = new Properties();

    private static void readXapiCodes() {
        try (FileInputStream fis = new FileInputStream("conf\\xapicodes.xml")) {
            xapiCodes.loadFromXML(fis);
        } catch (IOException e) {
            Logging.error(e);
        }
    }

    public static String getErrorDesc(String errorCode) {
        return xapiCodes.getProperty(errorCode, "Undefined error occured");
    }

    private void logResults(long startTime, boolean isAutoRecoveryInitiated) {
        long totalTime = System.currentTimeMillis() - startTime;
        Logging.info("Total Time taken in milliseconds: " + totalTime);

        // Log final results to database
        try {
            alertsDao.logResults(total, lowFunds.get(), posted.get(), failed.get(), totalCharge.get());
        } catch (Exception e) {
            Logging.error("Failed to log final results", e);
        }

        ReportService.generateChargeReport(isAutoRecoveryInitiated, posted.get(), failed.get()
                , lowFunds.get(), syserr.get(), processedRecords.get(), total);

        Logging.info("Method Exit: ChargePosting.run");
    }


}
