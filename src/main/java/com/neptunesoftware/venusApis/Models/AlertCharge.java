package com.neptunesoftware.venusApis.Models;

import java.math.BigDecimal;

public class AlertCharge {

	private int minValue;
	private int maxValue;
	private int smsAlertCrncyId;
	private BigDecimal vendorCharge;
	private BigDecimal exciseCharge;
	private BigDecimal bankCharge;
	private BigDecimal totalCharge;

	// Default constructor
	public AlertCharge() {}

	// All-args constructor
	public AlertCharge(int minValue, int maxValue, BigDecimal vendorCharge,
					   BigDecimal exciseCharge, BigDecimal bankCharge, int smsAlertCrncyId) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.vendorCharge = vendorCharge;
		this.exciseCharge = exciseCharge;
		this.bankCharge = bankCharge;
		this.smsAlertCrncyId = smsAlertCrncyId;
	}


	// Getters and setters following JavaBean conventions
	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}



	public BigDecimal getVendorCharge() {
		return vendorCharge;
	}

	public void setVendorCharge(BigDecimal vendorCharge) {
		this.vendorCharge = vendorCharge;
	}

	public BigDecimal getExciseCharge() {
		return exciseCharge;
	}

	public void setExciseCharge(BigDecimal exciseCharge) {
		this.exciseCharge = exciseCharge;
	}

	public BigDecimal getBankCharge() {
		return bankCharge;
	}

	public void setBankCharge(BigDecimal bankCharge) {
		this.bankCharge = bankCharge;
	}



	public int getSmsAlertCrncyId() {return smsAlertCrncyId;}

	public void setSmsAlertCrncyId(int smsAlertCrncyId) {this.smsAlertCrncyId = smsAlertCrncyId;}

	public BigDecimal getTotalCharge() {
		BigDecimal total = BigDecimal.ZERO;
		if (vendorCharge != null) total = total.add(vendorCharge);
		if (exciseCharge != null) total = total.add(exciseCharge);
		if (bankCharge != null) total = total.add(bankCharge);
		return total;
	}

}
