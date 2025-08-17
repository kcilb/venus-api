package com.neptunesoftware.venusApis.Controller;

import com.neptunesoftware.venusApis.DTOs.ChargeTierDTO;
import com.neptunesoftware.venusApis.Models.SmsAlertCurrency;
import com.neptunesoftware.venusApis.Services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis/v2/admin")
public class Admin {

    private final AdminService adminService;

    public Admin(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(value = "find-currency", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> findInstitutionCurrencies(@RequestBody SmsAlertCurrency request) {
        return ResponseEntity.ok(adminService.findInstitutionCurrencies(request.getSmsAlertCrncyId()));
    }

    @PostMapping(value = "find-sms-alert-currencies", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> findSMSAlertCurrencies(@RequestBody SmsAlertCurrency request) {
        return ResponseEntity.ok(adminService.findSMSAlertCurrencies(request.getSmsAlertCrncyId()));
    }

    @PostMapping(value = "maintain-currency", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> maintainSMSAlertCurrency(@RequestBody SmsAlertCurrency request) {
        return ResponseEntity.ok(adminService.maintainSMSAlertCurrency(request));
    }

    @PostMapping(value = "find-currency-charge-tier", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> findCharges(@RequestBody ChargeTierDTO request) {
        return ResponseEntity.ok(adminService.findCharges(request.getSmsAlertCrncyId()));
    }

    @PostMapping(value = "maintain-currency-charge-tier", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> maintainCharge(@RequestBody ChargeTierDTO request) {
        return ResponseEntity.ok(adminService.maintainCharge(request.getChargeTiers()));
    }
}
