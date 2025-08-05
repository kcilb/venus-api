// 
// Decompiled by Procyon v0.5.36
// 

package com.neptunesoftware.venusApis.Models;

import java.io.Serializable;

public class SMS implements Serializable
{
    private int recordID;
    private int customerId;
    private int tranId;
    private String custName;
    private String phoneNo;
    private String email;
    private String accountNo;
    private String tranPoint;
    private String tranType;
    private String tranTime;
    private String tranChannel;
    private String tranNaration;
    private String tranCurrency;
    private String acctOpenDate;
    private String acctDateOfLastActivationFromDormancy;
    private String domicileBranch;
    private String sysProcDate;
    private String tranAmount;
    private String acctBalAfterTran;
    private double acctHolderIncome;
    
    public int getRecordID() {
        return this.recordID;
    }
    
    public void setRecordID(final int recordID) {
        this.recordID = recordID;
    }
    
    public int getCustomerId() {
        return this.customerId;
    }
    
    public void setCustomerId(final int customerId) {
        this.customerId = customerId;
    }
    
    public int getTranId() {
        return this.tranId;
    }
    
    public void setTranId(final int tranId) {
        this.tranId = tranId;
    }
    
    public String getCustName() {
        return this.custName;
    }
    
    public void setCustName(final String custName) {
        this.custName = custName;
    }
    
    public String getPhoneNo() {
        return this.phoneNo;
    }
    
    public void setPhoneNo(final String phoneNo) {
        this.phoneNo = phoneNo;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public String getAccountNo() {
        return this.accountNo;
    }
    
    public void setAccountNo(final String accountNo) {
        this.accountNo = accountNo;
    }
    
    public String getTranPoint() {
        return this.tranPoint;
    }
    
    public void setTranPoint(final String tranPoint) {
        this.tranPoint = tranPoint;
    }
    
    public String getTranType() {
        return this.tranType;
    }
    
    public void setTranType(final String tranType) {
        this.tranType = tranType;
    }
    
    public String getTranTime() {
        return this.tranTime;
    }
    
    public void setTranTime(final String tranTime) {
        this.tranTime = tranTime;
    }
    
    public String getTranChannel() {
        return this.tranChannel;
    }
    
    public void setTranChannel(final String tranChannel) {
        this.tranChannel = tranChannel;
    }
    
    public String getTranNaration() {
        return this.tranNaration;
    }
    
    public void setTranNaration(final String tranNaration) {
        this.tranNaration = tranNaration;
    }
    
    public String getTranCurrency() {
        return this.tranCurrency;
    }
    
    public void setTranCurrency(final String tranCurrency) {
        this.tranCurrency = tranCurrency;
    }
    
    public String getAcctOpenDate() {
        return this.acctOpenDate;
    }
    
    public void setAcctOpenDate(final String acctOpenDate) {
        this.acctOpenDate = acctOpenDate;
    }
    
    public String getAcctDateOfLastActivationFromDormancy() {
        return this.acctDateOfLastActivationFromDormancy;
    }
    
    public void setAcctDateOfLastActivationFromDormancy(final String acctDateOfLastActivationFromDormancy) {
        this.acctDateOfLastActivationFromDormancy = acctDateOfLastActivationFromDormancy;
    }
    
    public String getDomicileBranch() {
        return this.domicileBranch;
    }
    
    public void setDomicileBranch(final String domicileBranch) {
        this.domicileBranch = domicileBranch;
    }
    
    public String getSysProcDate() {
        return this.sysProcDate;
    }
    
    public void setSysProcDate(final String sysProcDate) {
        this.sysProcDate = sysProcDate;
    }
    
    public String getTranAmount() {
        return this.tranAmount;
    }
    
    public void setTranAmount(final String tranAmount) {
        this.tranAmount = tranAmount;
    }
    
    public String getAcctBalAfterTran() {
        return this.acctBalAfterTran;
    }
    
    public void setAcctBalAfterTran(final String acctBalAfterTran) {
        this.acctBalAfterTran = acctBalAfterTran;
    }
    
    public double getAcctHolderIncome() {
        return this.acctHolderIncome;
    }
    
    public void setAcctHolderIncome(final double acctHolderIncome) {
        this.acctHolderIncome = acctHolderIncome;
    }
}
