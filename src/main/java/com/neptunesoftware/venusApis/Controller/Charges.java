package com.neptunesoftware.venusApis.Controller;

import com.neptunesoftware.venusApis.Beans.AppProps;
import com.neptunesoftware.venusApis.DTOs.ChargeProcessDTO;
import com.neptunesoftware.venusApis.Services.ChargeProcessService;
import com.neptunesoftware.venusApis.Services.ChargeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis/v2/charges")
public class Charges {

    private final AppProps appProps;
    private final ChargeService chargeService;
    private final ChargeProcessService chargeProcessService;

    public Charges(AppProps appProps, ChargeService chargeService, ChargeProcessService chargeProcessService) {
        this.appProps = appProps;
        this.chargeService = chargeService;
        this.chargeProcessService = chargeProcessService;
    }

    @PostMapping(value = "process-charges", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> processCharges(@RequestBody ChargeProcessDTO request) {
        String resultView = request.isAutoRecoveryInitiated ? appProps.failedCharge : appProps.pendingCharge;
        return ResponseEntity.ok(chargeProcessService.processSMSCharges(resultView,
                request.isAutoRecoveryInitiated, request.currencyId));
    }

    @PostMapping(value = "find-charge-history", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> findChargeHistory(@RequestBody ChargeProcessDTO request) {
        return ResponseEntity.ok(chargeService.findChargeHistory(request));
    }
}
