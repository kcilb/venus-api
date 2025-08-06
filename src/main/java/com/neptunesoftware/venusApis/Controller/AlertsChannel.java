package com.neptunesoftware.venusApis.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neptunesoftware.venusApis.Services.AlertChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/apis/v2/alerts-channel")
public class AlertsChannel {

    private final AlertChannelService alertChannelService;

    public AlertsChannel(AlertChannelService alertChannelService) {
        this.alertChannelService = alertChannelService;
    }

    @PostMapping(value = "findTransactionAlerts",consumes = "application/json",produces = "application/json")
    public ResponseEntity<?> findTransactionAlerts(@RequestBody String body) {
        String lastMsgId = "";
        return ResponseEntity.ok(alertChannelService.findTransactionAlerts(lastMsgId));
    }

    @PostMapping(value = "updateAccountStats", consumes = "application/json",produces = "application/json")
    public ResponseEntity<?> updateAccountStats(@RequestBody String body) {
        return ResponseEntity.ok(alertChannelService.updateAccountStats(body));
    }
}
