package com.internshiproject.portfolioManagement.controller;

import com.internshiproject.portfolioManagement.entity.User;
import com.internshiproject.portfolioManagement.repository.UserRepository;
import com.internshiproject.portfolioManagement.service.JwtService;
import com.internshiproject.portfolioManagement.service.RiskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk")
@CrossOrigin("*")
public class RiskController {

    @Autowired
    private RiskService riskService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public String checkRisk(
            @RequestHeader("Authorization") String token) {

        String email =
                jwtService.extractUsername(
                        token.replace("Bearer ", "")
                );

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(
                                () -> new RuntimeException("User not found")
                        );

        return riskService.checkRisk(user);
    }
}
