package com.neptunesoftware.venusApis.Controller;

import com.neptunesoftware.venusApis.Security.PropsSecurity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis/v2/extras")
public class Extras {

    @PostMapping(value = "encrypt", consumes = "application/json",produces = "application/json")
    public ResponseEntity<String> encrypt(@RequestBody String body) {
        return ResponseEntity.ok(PropsSecurity.encrypt(body));
    }
    @PostMapping(value = "decrypt", consumes = "application/json",produces = "application/json")
    public ResponseEntity<String> decrypt(@RequestBody String body) {
        return ResponseEntity.ok(PropsSecurity.decrypt(body));
    }

}
