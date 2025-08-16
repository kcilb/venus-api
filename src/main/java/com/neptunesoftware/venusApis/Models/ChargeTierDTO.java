package com.neptunesoftware.venusApis.Models;

import lombok.Data;

import java.util.List;

public class ChargeTierDTO {
    public List<ChargeTiers> chargeTiers;
    private Integer smsAlertCrncyId;
    private boolean isRemove;

    // Default constructor
    public ChargeTierDTO() {
    }

    // Getter and Setter for chargeTiers
    public List<ChargeTiers> getChargeTiers() {
        return chargeTiers;
    }

    public void setChargeTiers(List<ChargeTiers> chargeTiers) {
        this.chargeTiers = chargeTiers;
    }

    // Getter and Setter for smsAlertCrncyId
    public Integer getSmsAlertCrncyId() {
        return smsAlertCrncyId;
    }

    public void setSmsAlertCrncyId(Integer smsAlertCrncyId) {
        this.smsAlertCrncyId = smsAlertCrncyId;
    }

    // Getter and Setter for isRemove
    // Note: Boolean getter typically uses 'is' prefix
    public boolean isRemove() {
        return isRemove;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }


}
