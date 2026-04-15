package com.example.portback.controller;

import com.example.portback.config.JwtUtil;
import com.example.portback.dto.LoginRequest;
import com.example.portback.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final JwtUtil jwtUtil;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (adminUsername.equals(request.getUsername()) &&
                adminPassword.equals(request.getPassword())) {
            String token = jwtUtil.generateToken("admin");
            return ResponseEntity.ok(new LoginResponse(token));
        }
        return ResponseEntity.status(401).body("{\"error\": \"Invalid credentials\"}");
    }
}
