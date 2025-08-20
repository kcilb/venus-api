package com.neptunesoftware.venusApis.Models;

import java.math.BigDecimal;
import java.sql.Date;

public class SMSChargeLog implements java.io.Serializable {
    private Integer totalAccounts;
    private Integer lowFundsCount;
    private Integer processedCount;
    private Integer failedCount;
    private BigDecimal recoveredAmt;
    private String chargeDesc;
    private Date createDt;
    private String status;

    public SMSChargeLog() {}

    SMSChargeLog(Integer totalAccounts, Integer lowFundsCount, Integer processedCount, Integer failedCount, BigDecimal recoveredAmt, String chargeDesc, Date createDt, String status) {
        this.totalAccounts = totalAccounts;
        this.lowFundsCount = lowFundsCount;
        this.processedCount = processedCount;
        this.failedCount = failedCount;
        this.recoveredAmt = recoveredAmt;
        this.chargeDesc = chargeDesc;
        this.createDt = createDt;
        this.status = status;
    }

    public Integer getTotalAccounts() {
        return totalAccounts;
    }

    public void setTotalAccounts(Integer totalAccounts) {
        this.totalAccounts = totalAccounts;
    }

    public Integer getLowFundsCount() {
        return lowFundsCount;
    }

    public void setLowFundsCount(Integer lowFundsCount) {
        this.lowFundsCount = lowFundsCount;
    }

    public Integer getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(Integer processedCount) {
        this.processedCount = processedCount;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    public BigDecimal getRecoveredAmt() {
        return recoveredAmt;
    }

    public void setRecoveredAmt(BigDecimal recoveredAmt) {
        this.recoveredAmt = recoveredAmt;
    }

    public String getChargeDesc() {
        return chargeDesc;
    }

    public void setChargeDesc(String chargeDesc) {
        this.chargeDesc = chargeDesc;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
