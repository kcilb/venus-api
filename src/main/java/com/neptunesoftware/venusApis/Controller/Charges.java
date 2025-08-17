package com.neptunesoftware.venusApis.Controller;

import com.neptunesoftware.venusApis.DTOs.ChargeProcessDTO;
import com.neptunesoftware.venusApis.Services.ChargeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis/v2/admin")
public class Charges {
    private final ChargeService chargeService;

    public Charges(ChargeService chargeService) {
        this.chargeService = chargeService;
    }

    @PostMapping(value = "process-charges", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> processCharges(@RequestBody ChargeProcessDTO request) {
        return ResponseEntity.ok(chargeService.processSMSCharges(request.resultSetView,
                request.isAutoRecoveryInitiated));
    }
}
