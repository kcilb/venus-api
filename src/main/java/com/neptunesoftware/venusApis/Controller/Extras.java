package com.neptunesoftware.venusApis.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neptunesoftware.venusApis.Security.PropsSecurity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/apis/v2/extras")
public class Extras {

    @PostMapping(value = "encrypt", consumes = "application/json",produces = "application/json")
    public ResponseEntity<String> encrypt(@RequestBody String body) throws JsonProcessingException {
        Map<String,String> key = new ObjectMapper().readValue(body, Map.class);
        return ResponseEntity.ok(PropsSecurity.encrypt(key.get("body")));
    }
    @PostMapping(value = "decrypt", consumes = "application/json",produces = "application/json")
    public ResponseEntity<String> decrypt(@RequestBody String body) throws JsonProcessingException {
        Map<String,String> key = new ObjectMapper().readValue(body, Map.class);
        return ResponseEntity.ok(PropsSecurity.decrypt(key.get("body")));
    }

}
