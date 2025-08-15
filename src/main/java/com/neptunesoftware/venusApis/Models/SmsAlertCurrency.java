package com.neptunesoftware.venusApis.Models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class SmsAlertCurrency implements Serializable {
    // Getters and Setters
    @Setter
    @Getter
    private Object smsAlertCrncyId; // Can be Integer or String
    @Setter
    @Getter
    private String crncyIso;
    @Setter
    @Getter
    private int crncyId;
    @Getter
    @Setter
    private Status status;

    @Getter
    @Setter
    private String crncyNm;


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

    public SmsAlertCurrency(Object smsAlertCrncyId, String crncyIso, int crncyId, Status status, String crncyNm) {
        this.smsAlertCrncyId = smsAlertCrncyId;
        this.crncyIso = crncyIso;
        this.crncyId = crncyId;
        this.status = status;
        this.crncyNm = crncyNm;
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
