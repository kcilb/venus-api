package com.neptunesoftware.venusApis.Models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


public class SmsAlertCurrency implements Serializable {


    private Integer smsAlertCrncyId;

    private String crncyIso;

    private Integer crncyId;

    private String crncyNm;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String createdBy;

    private String modifiedBy;

    private String status;

    public SmsAlertCurrency() {
    }

    public SmsAlertCurrency(Integer smsAlertCrncyId, String crncyIso, Integer crncyId, String crncyNm,
                            LocalDateTime createdDate, LocalDateTime modifiedDate,
                            String createdBy, String modifiedBy, String status) {
        this.smsAlertCrncyId = smsAlertCrncyId;
        this.crncyIso = crncyIso;
        this.crncyId = crncyId;
        this.crncyNm = crncyNm;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.status = status;
    }

    public Integer getSmsAlertCrncyId() {
        return smsAlertCrncyId;
    }

    public void setSmsAlertCrncyId(Integer smsAlertCrncyId) {
        this.smsAlertCrncyId = smsAlertCrncyId;
    }

    public String getCrncyIso() {
        return crncyIso;
    }

    public void setCrncyIso(String crncyIso) {
        this.crncyIso = crncyIso;
    }

    public Integer getCrncyId() {
        return crncyId;
    }

    public void setCrncyId(Integer crncyId) {
        this.crncyId = crncyId;
    }

    public String getCrncyNm() {
        return crncyNm;
    }

    public void setCrncyNm(String crncyNm) {
        this.crncyNm = crncyNm;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
