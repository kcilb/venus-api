package com.neptunesoftware.venusApis.Services;

import com.neptunesoftware.venusApis.JwtManager.JwtUtil;
import com.neptunesoftware.venusApis.Models.ApiResponse;
import com.neptunesoftware.venusApis.Models.AuthRequest;
import com.neptunesoftware.venusApis.Util.Logging;
import com.neptunesoftware.venusApis.Util.StaticRefs;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public ApiResponse<?> login(AuthRequest request) {
        try {
            List<String> roles = List.of("ADMIN");
            Authentication userData = JwtUtil.createAuthentication(request.username, roles);
            String token = jwtUtil.generateToken(userData);
            return ApiResponse.builder().data(token)
                    .response(StaticRefs.success()).build();
        } catch (Exception e) {
            Logging.info(e.getMessage());
            return ApiResponse.builder().data(null)
                    .response(StaticRefs.serverError()).build();
        }
    }
}
