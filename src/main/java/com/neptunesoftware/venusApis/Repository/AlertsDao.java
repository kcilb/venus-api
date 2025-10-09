package com.neptunesoftware.venusApis.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neptunesoftware.venusApis.Beans.AppProps;
import com.neptunesoftware.venusApis.Models.*;
import com.neptunesoftware.venusApis.Util.Logging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Repository
@Slf4j
public class AlertsDao {

    private final JdbcTemplate jdbcTemplate;
    private final AppProps appProps;

    public AlertsDao(JdbcTemplate jdbcTemplate, AppProps appProps) {
        this.jdbcTemplate = jdbcTemplate;
        this.appProps = appProps;
    }


    public TrxnSmsList findTransactionAlerts(String lastMsgId) {
        TrxnSmsList tranList;
        try {
            Integer lastMessageId = (lastMsgId != null)
                    && (!lastMsgId.trim().isEmpty()) ? Integer.parseInt(lastMsgId)
                    : 0;
            int fetchLimit = appProps.fetchLimit;

            List<SMS> messageList = jdbcTemplate
                    .query("select * from v_outward_messages_dep where recordID > ? and rownum <= ?",
                            new BeanPropertyRowMapper<>(
                                    SMS.class), lastMessageId, fetchLimit);
            ObjectMapper mapper = new ObjectMapper();
            if (!messageList.isEmpty())
                tranList = new TrxnSmsList("0", "Success", messageList);
            else
                tranList = new TrxnSmsList("21", "No records available", null);

        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            tranList = new TrxnSmsList("96",
                    "An error occurred while processing your request.", null);
        } finally {
            Logging.info(
                    "Last retrieved transaction alert id: " + lastMsgId);
        }
        return tranList;
    }

    public int getTotalRecords(String resultSetView, Integer currencyId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM " + resultSetView + " WHERE SMS_ALERT_CRNCY_ID = ?",
                    Integer.class,
                    currencyId
            );
        } catch (EmptyResultDataAccessException e) {
            throw e;
        }
    }

    public List<AlertRequest> findPendingCharges(int pageNo, int pageSize, int currency, String resultSetView) {
        try {
            int pageNumber = (pageNo - 1) * pageSize;
            int offset = pageSize;
            String sql = "SELECT * FROM " + resultSetView + " WHERE SMS_ALERT_CRNCY_ID = ? ORDER BY ACCT_NO OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            return jdbcTemplate.query(sql,
                    new BeanPropertyRowMapper<>(AlertRequest.class),currency, pageNumber, offset
            );
        } catch (EmptyResultDataAccessException e) {
            throw e;
        }
    }

    private Optional<Map<String, Object>> getAlertCount(String acctNo) {
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(
                    "SELECT * FROM v_alert_count WHERE acct_no = ?",
                    acctNo
            );
            return Optional.of(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Transactional
    public Update updateAccountStats(String acctNo, int msgCount, String processDt) {
        try {
            String updateSql = "update sms_count set sms_count = ?, total_count = ?, chrge_status =? where acct_no = ? ";

            String insertSql = "insert into sms_count(sms_count,total_count,chrge_status,acct_no,log_date) " +
                    "values(?,?,?,?,?)";


            Optional<Map<String, Object>> map = getAlertCount(acctNo);
            int updateCount = 0;
            if (map.isPresent()) {
                int totalSMS = Integer.parseInt(String.valueOf(map
                        .get().get("sms_count"))) + msgCount;
                updateCount = jdbcTemplate.update(updateSql, totalSMS, totalSMS, "N", acctNo);
            } else {
                Logging.info("PROCESSING_DATE:"+processDt);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate coreDate = LocalDate.parse(processDt, formatter);
                updateCount = jdbcTemplate.update(insertSql, msgCount, msgCount, "N", acctNo, coreDate);
            }

            if (updateCount >= 0) {
                return new Update("0", "Success", null);
            }
            return new Update("92", "No updates were performed", null);
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            return new Update("96",
                    "An error occurred while processing your request "
                            + e.getLocalizedMessage(), null);
        } finally {
            Logging.info(
                    "updateAccountStats account number: " + acctNo
                            + " last sent out alerts count " + msgCount);
        }
    }

    public void updateSMSCount(String acctNo, String message, String status
            , BigDecimal chargeAmount, int smsCount, java.sql.Date logDate) {
        try {
            String sql = "update SMS_COUNT set charge_mode='Tiered', chrge_status=?, sms_count=?, charge_amount=?, tran_date=?, " +
                    "error_reason=? where acct_no =? and sms_count=? and log_date=?";

            jdbcTemplate.update(sql,
                    status,
                    "C".equals(status) ? 0 : smsCount,
                    "C".equals(status) ? chargeAmount : BigDecimal.ZERO,
                    new java.sql.Date(new Date().getTime()),
                    message,
                    acctNo,
                    smsCount,
                    logDate
            );
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            throw e;
        }
    }


    public void logFailedSplit(AlertRequest chargeData) {
        try {
            String sql = "INSERT INTO SMSBANK.FAILED_SPLIT VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, SYSDATE)";

            jdbcTemplate.update(sql,
                    chargeData.getAcctNo(),
                    chargeData.getBankCharge(),
                    chargeData.getVendorCharge(),
                    chargeData.getTaxCharge(),
                    "Y",
                    "N",
                    "Y",
                    chargeData.getBankChargeGL(),
                    chargeData.getVendorChargeGL(),
                    chargeData.getTaxChargeGL(),
                    chargeData.getSmsCount(),
                    "P",
                    chargeData.getChargeDesc(),
                    chargeData.getVendorCharge(),
                    new java.sql.Date(chargeData.getLogDate().getTime()),
                    chargeData.getCrncyCdIso(),
                    chargeData.getTxnReference()
            );
        } catch (Exception ex) {
            Logging.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public void logResults(int total, int lowFunds, int posted, int failed, BigDecimal totalCharge,String currencyName) {
        try {
            String sql = "INSERT INTO SMSBANK.SMS_CHARGE_LOG " +
                    "(TOTAL_ACCOUNTS, LOW_FUNDS_COUNT, PROCESSED_COUNT, FAILED_COUNT, RECOVERED_AMT, " +
                    "CHARGE_DESC, CREATE_DT, STATUS) VALUES(?, ?, ?, ?, ?, ?, SYSDATE, ?)";

            jdbcTemplate.update(sql,
                    total,
                    lowFunds,
                    posted,
                    failed,
                    totalCharge,
                    "Monthly SMS charge routine for "+currencyName,
                    "P"
            );
        } catch (Exception ex) {
            Logging.error(ex.getMessage(), ex);
            throw ex;
        }
    }


}
