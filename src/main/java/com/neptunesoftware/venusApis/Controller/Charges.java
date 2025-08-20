package com.neptunesoftware.venusApis.Controller;

import com.neptunesoftware.venusApis.DTOs.ChargeProcessDTO;
import com.neptunesoftware.venusApis.Services.ChargeProcessService;
import com.neptunesoftware.venusApis.Services.ChargeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis/v2/charges")
public class Charges {
    private final ChargeService chargeService;
    private final ChargeProcessService chargeProcessService;

    public Charges(ChargeService chargeService, ChargeProcessService chargeProcessService) {
        this.chargeService = chargeService;
        this.chargeProcessService = chargeProcessService;
    }

    @PostMapping(value = "process-charges", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> processCharges(@RequestBody ChargeProcessDTO request) {
        String resultView = request.isAutoRecoveryInitiated ? "V_FAILED_CHARGES" : "V_PENDING_CHARGES";
        return ResponseEntity.ok(chargeProcessService.processSMSCharges(resultView,
                request.isAutoRecoveryInitiated));
    }

    @PostMapping(value = "charge-history", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> chargeHistory(@RequestBody ChargeProcessDTO request) {
        return ResponseEntity.ok(chargeService.findChargeHistory(request));
    }
}
