package com.neptunesoftware.venusApis.Models;

import java.io.Serializable;

public class SmsAlertCurrency implements Serializable {
    private Object smsAlertCrncyId; // Can be Integer or String
    private String crncyIso;
    private int crncyId;
    private Status status;

    // Enum for status values
    public enum Status {
        ACTIVE,
        INACTIVE,
        PENDING,
        ARCHIVED
    }

    // Constructors
    public SmsAlertCurrency() {
    }

    public SmsAlertCurrency(Object smsAlertCrncyId, String crncyIso, int crncyId, Status status) {
        this.smsAlertCrncyId = smsAlertCrncyId;
        this.crncyIso = crncyIso;
        this.crncyId = crncyId;
        this.status = status;
    }

    // Getters and Setters
    public Object getSmsAlertCrncyId() {
        return smsAlertCrncyId;
    }

    public void setSmsAlertCrncyId(Object smsAlertCrncyId) {
        this.smsAlertCrncyId = smsAlertCrncyId;
    }

    public String getCrncyIso() {
        return crncyIso;
    }

    public void setCrncyIso(String crncyIso) {
        this.crncyIso = crncyIso;
    }

    public int getCrncyId() {
        return crncyId;
    }

    public void setCrncyId(int crncyId) {
        this.crncyId = crncyId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // toString method
    @Override
    public String toString() {
        return "SmsAlertCurrency{" +
                "smsAlertCrncyId=" + smsAlertCrncyId +
                ", crncyIso='" + crncyIso + '\'' +
                ", crncyId=" + crncyId +
                ", status=" + status +
                '}';
    }
}
