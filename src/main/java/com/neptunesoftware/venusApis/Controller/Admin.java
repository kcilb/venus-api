package com.neptunesoftware.venusApis.Controller;

import com.neptunesoftware.venusApis.Models.SmsAlertCurrency;
import com.neptunesoftware.venusApis.Services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/apis/v2/admin")
public class Admin {

    private final AdminService adminService;

    public Admin(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(value = "findInstitutionCurrencies", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> findInstitutionCurrencies() {
        return ResponseEntity.ok(adminService.findInstitutionCurrencies());
    }

    @PostMapping(value = "findSMSAlertCurrencies", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> findSMSAlertCurrencies(@RequestBody Integer id) {
        return ResponseEntity.ok(adminService.findSMSAlertCurrencies(id));
    }

    @PostMapping(value = "maintainSMSAlertCurrency", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> maintainSMSAlertCurrency(@RequestBody SmsAlertCurrency request) {
        return ResponseEntity.ok(adminService.maintainSMSAlertCurrency(request));
    }

    @PostMapping(value = "findCharges", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> findCharges(@RequestBody Integer id) {
        return ResponseEntity.ok(adminService.findCharges(id));
    }

    @PostMapping(value = "maintainCharge", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> maintainCharge(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(adminService.maintainCharge(request));
    }
}
