package com.neptunesoftware.venusApis.Models;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public class ChargeTiers implements Serializable {
    private Long id; // Using Long instead of primitive to handle null
    private Long smsAlertCrncyId;
    private String chargeDesc;
    private String txnType;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private BigDecimal vendorCharge;
    private BigDecimal bankCharge;
    private BigDecimal exciseCharge;
    private String status; // Default to 'A'
    private String modifiedBy;
    private LocalDateTime modifiedDate;

    public ChargeTiers() {
    }

    // All-args constructor
    public ChargeTiers(Long id, Long smsAlertCrncyId, String chargeDesc, String txnType,
                       BigDecimal minValue, BigDecimal maxValue, BigDecimal vendorCharge,
                       BigDecimal bankCharge, BigDecimal exciseCharge, String status,
                       String modifiedBy, LocalDateTime modifiedDate) {
        this.id = id;
        this.smsAlertCrncyId = smsAlertCrncyId;
        this.chargeDesc = chargeDesc;
        this.txnType = txnType;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.vendorCharge = vendorCharge;
        this.bankCharge = bankCharge;
        this.exciseCharge = exciseCharge;
        this.status = status;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSmsAlertCrncyId() {
        return smsAlertCrncyId;
    }

    public void setSmsAlertCrncyId(Long smsAlertCrncyId) {
        this.smsAlertCrncyId = smsAlertCrncyId;
    }

    public String getChargeDesc() {
        return chargeDesc;
    }

    public void setChargeDesc(String chargeDesc) {
        this.chargeDesc = chargeDesc;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public BigDecimal getVendorCharge() {
        return vendorCharge;
    }

    public void setVendorCharge(BigDecimal vendorCharge) {
        this.vendorCharge = vendorCharge;
    }

    public BigDecimal getBankCharge() {
        return bankCharge;
    }

    public void setBankCharge(BigDecimal bankCharge) {
        this.bankCharge = bankCharge;
    }

    public BigDecimal getExciseCharge() {
        return exciseCharge;
    }

    public void setExciseCharge(BigDecimal exciseCharge) {
        this.exciseCharge = exciseCharge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }


}
