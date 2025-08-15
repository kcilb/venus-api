package com.neptunesoftware.venusApis.Models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
public class SmsAlertCurrency {

    private String smsAlertCrncyId;

    private String crncyIso;

    private Integer crncyId;

    private String crncyNm;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String createdBy;

    private String modifiedBy;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = LocalDateTime.now();
    }
}
