package com.neptunesoftware.venusApis.Controller;

import com.neptunesoftware.venusApis.Models.AuthRequest;
import com.neptunesoftware.venusApis.Models.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("apis/v2/auth")
public class Auth {

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok("");
    }
}
