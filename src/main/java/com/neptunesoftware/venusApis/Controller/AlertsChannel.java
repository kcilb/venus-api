package com.neptunesoftware.venusApis.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis/v2/alerts-channel")
public class AlertsChannel {

    @PostMapping(value = "findTransactionAlerts", consumes = "application/json",produces = "application/json")
    public ResponseEntity<String> findTransactionAlerts(@RequestBody String body) {
        return ResponseEntity.ok("hello");
    }

    @PostMapping(value = "updateAccountStats", consumes = "application/json",produces = "application/json")
    public ResponseEntity<String> updateAccountStats(@RequestBody String body) {
        return ResponseEntity.ok("hello");
    }
}
