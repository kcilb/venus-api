package com.neptunesoftware.venusApis.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neptunesoftware.venusApis.JwtManager.JwtUtil;
import com.neptunesoftware.venusApis.Security.PropsSecurity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Encoders;
import javax.crypto.SecretKey;

import java.util.List;
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

    @PostMapping(value = "process", consumes = "application/json",produces = "application/json")
    public ResponseEntity<?> process(@RequestBody String body) throws JsonProcessingException {
//        SecretKey key = Keys.secretKeyFor(Jwts.SIG.HS384);
//
//        // Convert to Base64 for storage in properties
//        String base64Key = Encoders.BASE64.encode(key.getEncoded());

        List<String> roles = List.of("Admin");
        Authentication auth = JwtUtil.createTestAuthentication("lubega",roles);
        return ResponseEntity.ok(new JwtUtil().validateToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsdWJlZ2EiLCJpYXQiOjE3NTU0NTU2MzMsImV4cCI6MTc1NTQ1NTYzMywicm9sZXMiOlsiQWRtaW4iXX0.q3dLxa4ztZyqG23GVDsAQ9jqExK8dxcs-USXPSHkqKg"));
    }

}
