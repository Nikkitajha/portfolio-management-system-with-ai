package com.internshiproject.portfolioManagement.controller;


import com.internshiproject.portfolioManagement.dto.RegisterDto;
import com.internshiproject.portfolioManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto) {
        try {
            userService.registerUser(dto);
            return ResponseEntity.ok("User registered successfully. OTP sent to email.");
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // SEND OTP (Login Step 1)
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
        try {
            String response = userService.sendOtp(email);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //  VERIFY + LOGIN (Login Step 2)
    @PostMapping("/verify-login")
    public ResponseEntity<?> verifyLogin(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String otp) {

        try {
            String token = userService.verifyLogin(email, password, otp);
            return ResponseEntity.ok(token); // JWT token
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}