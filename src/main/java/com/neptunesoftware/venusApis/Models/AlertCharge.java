package com.neptunesoftware.venusApis.Models;

import java.math.BigDecimal;

public class AlertCharge {

	private int min_value, max_value, smsAlertCrncyId;
	private BigDecimal vendor_charge, tax_charge, bank_charge, total_charge;

	public AlertCharge(int min_value, int max_value, BigDecimal vendor_charge, BigDecimal tax_charge,
                       BigDecimal bank_charge,int smsAlertCrncyId) {
		super();
		this.min_value = min_value;
		this.max_value = max_value;
		this.vendor_charge = vendor_charge;
		this.tax_charge = tax_charge;
		this.bank_charge = bank_charge;
		this.total_charge = vendor_charge.add(bank_charge).add(tax_charge);
		this.smsAlertCrncyId = smsAlertCrncyId;
	}

	public int getMin_value() {
		return min_value;
	}

	public void setMin_value(int min_value) {
		this.min_value = min_value;
	}

	public int getMax_value() {
		return max_value;
	}

	public void setMax_value(int max_value) {
		this.max_value = max_value;
	}

	public BigDecimal getVendor_charge() {
		return vendor_charge;
	}

	public void setVendor_charge(BigDecimal vendor_charge) {
		this.vendor_charge = vendor_charge;
	}

	public BigDecimal getTax_charge() {
		return tax_charge;
	}

	public void setTax_charge(BigDecimal tax_charge) {
		this.tax_charge = tax_charge;
	}

	public BigDecimal getBank_charge() {
		return bank_charge;
	}

	public void setBank_charge(BigDecimal bank_charge) {
		this.bank_charge = bank_charge;
	}

	public BigDecimal getTotal_charge() {
		return total_charge;
	}

	public void setTotal_charge(BigDecimal total_charge) {
		this.total_charge = total_charge;
	}


	public int getSmsAlertCrncyId() {return smsAlertCrncyId;}

	public void setSmsAlertCrncyId(int smsAlertCrncyId) {this.smsAlertCrncyId = smsAlertCrncyId;}

}
