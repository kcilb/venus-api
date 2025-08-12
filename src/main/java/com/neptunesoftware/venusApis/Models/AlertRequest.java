package com.neptunesoftware.venusApis.Models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author Olupot.D
 *
 */

@SuppressWarnings("serial")
public class AlertRequest implements Serializable {

	private String account, contraAccount, txnDesc, txnReference, txnCurrency, channelCode, xapiCode, cardNumber,
			chargeJournalId, bankChargeGL, taxChargeGL, vendorChargeGL, name, query, sprocedures, sender, chargeDesc,
			chargeAcct, reason, errorcode;
	private BigDecimal txnAmount, chargeAmount, bankCharge, taxCharge, vendorCharge;
	private long channelId, msgCount;
	private boolean vendorChargePosted, bankChargePosted, taxChargePosted;
	private int smsCount;
	private Date logDate;

	public AlertRequest(String account, String contraAccount, String txnDesc, String txnReference, String txnCurrency,
                        String channelCode, String xapiCode, String cardNumber, String chargeJournalId, String bankChargeGL,
                        String taxChargeGL, String vendorChargeGL, String name, String query, String sprocedures, String sender,
                        String chargeDesc, String chargeAcct, String reason, BigDecimal txnAmount, BigDecimal chargeAmount,
                        BigDecimal bankCharge, BigDecimal taxCharge, BigDecimal vendorCharge, long channelId, long msgCount,
                        boolean vendorChargePosted, boolean bankChargePosted, boolean taxChargePosted, int smsCount, Date logDate) {
		super();
		this.account = account;
		this.contraAccount = contraAccount;
		this.txnDesc = txnDesc;
		this.txnReference = txnReference;
		this.txnCurrency = txnCurrency;
		this.channelCode = channelCode;
		this.xapiCode = xapiCode;
		this.cardNumber = cardNumber;
		this.chargeJournalId = chargeJournalId;
		this.bankChargeGL = bankChargeGL;
		this.taxChargeGL = taxChargeGL;
		this.vendorChargeGL = vendorChargeGL;
		this.name = name;
		this.query = query;
		this.sprocedures = sprocedures;
		this.sender = sender;
		this.chargeDesc = chargeDesc;
		this.chargeAcct = chargeAcct;
		this.reason = reason;
		this.txnAmount = txnAmount;
		this.chargeAmount = chargeAmount;
		this.bankCharge = bankCharge;
		this.taxCharge = taxCharge;
		this.vendorCharge = vendorCharge;
		this.channelId = channelId;
		this.msgCount = msgCount;
		this.vendorChargePosted = vendorChargePosted;
		this.bankChargePosted = bankChargePosted;
		this.taxChargePosted = taxChargePosted;
		this.smsCount = smsCount;
		this.logDate = logDate;
	}

	public AlertRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getContraAccount() {
		return contraAccount;
	}

	public void setContraAccount(String contraAccount) {
		this.contraAccount = contraAccount;
	}

	public String getTxnDesc() {
		return txnDesc;
	}

	public void setTxnDesc(String txnDesc) {
		this.txnDesc = txnDesc;
	}

	public String getTxnReference() {
		return txnReference;
	}

	public void setTxnReference(String txnReference) {
		this.txnReference = txnReference;
	}

	public String getTxnCurrency() {
		return txnCurrency;
	}

	public void setTxnCurrency(String txnCurrency) {
		this.txnCurrency = txnCurrency;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getXapiCode() {
		return xapiCode;
	}

	public void setXapiCode(String xapiCode) {
		this.xapiCode = xapiCode;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getChargeJournalId() {
		return chargeJournalId;
	}

	public void setChargeJournalId(String chargeJournalId) {
		this.chargeJournalId = chargeJournalId;
	}

	public String getBankChargeGL() {
		return bankChargeGL;
	}

	public void setBankChargeGL(String bankChargeGL) {
		this.bankChargeGL = bankChargeGL;
	}

	public String getTaxChargeGL() {
		return taxChargeGL;
	}

	public void setTaxChargeGL(String taxChargeGL) {
		this.taxChargeGL = taxChargeGL;
	}

	public String getVendorChargeGL() {
		return vendorChargeGL;
	}

	public void setVendorChargeGL(String vendorChargeGL) {
		this.vendorChargeGL = vendorChargeGL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getSprocedures() {
		return sprocedures;
	}

	public void setSprocedures(String sprocedures) {
		this.sprocedures = sprocedures;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getChargeDesc() {
		return chargeDesc;
	}

	public void setChargeDesc(String chargeDesc) {
		this.chargeDesc = chargeDesc;
	}

	public String getChargeAcct() {
		return chargeAcct;
	}

	public void setChargeAcct(String chargeAcct) {
		this.chargeAcct = chargeAcct;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public BigDecimal getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(BigDecimal txnAmount) {
		this.txnAmount = txnAmount;
	}

	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public BigDecimal getBankCharge() {
		return bankCharge;
	}

	public void setBankCharge(BigDecimal bankCharge) {
		this.bankCharge = bankCharge;
	}

	public BigDecimal getTaxCharge() {
		return taxCharge;
	}

	public void setTaxCharge(BigDecimal taxCharge) {
		this.taxCharge = taxCharge;
	}

	public BigDecimal getVendorCharge() {
		return vendorCharge;
	}

	public void setVendorCharge(BigDecimal vendorCharge) {
		this.vendorCharge = vendorCharge;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public long getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(long msgCount) {
		this.msgCount = msgCount;
	}

	public boolean isVendorChargePosted() {
		return vendorChargePosted;
	}

	public void setVendorChargePosted(boolean vendorChargePosted) {
		this.vendorChargePosted = vendorChargePosted;
	}

	public boolean isBankChargePosted() {
		return bankChargePosted;
	}

	public void setBankChargePosted(boolean bankChargePosted) {
		this.bankChargePosted = bankChargePosted;
	}

	public boolean isTaxChargePosted() {
		return taxChargePosted;
	}

	public void setTaxChargePosted(boolean taxChargePosted) {
		this.taxChargePosted = taxChargePosted;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	
	public int getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(int smsCount) {
		this.smsCount = smsCount;
	}
	
	
	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	
	
	
	
	

}
