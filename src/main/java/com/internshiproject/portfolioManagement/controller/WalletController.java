package com.internshiproject.portfolioManagement.controller;
import com.internshiproject.portfolioManagement.dto.ApiResponseDto;
import com.internshiproject.portfolioManagement.dto.WalletTransactionDto;
import com.internshiproject.portfolioManagement.entity.User;
import com.internshiproject.portfolioManagement.entity.Wallet;
import com.internshiproject.portfolioManagement.repository.UserRepository;
import com.internshiproject.portfolioManagement.service.JwtService;
import com.internshiproject.portfolioManagement.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin("*")

public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    // ================= COMMON METHOD =================
    private User getUserFromToken(String token) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ================= GET WALLET =================
    @GetMapping("/details")
    public Wallet getWallet(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        return walletService.getWallet(user.getId());
    }

    // ================= ADD FUNDS =================
    @PostMapping("/add")
    public ApiResponseDto addFunds(
            @RequestHeader("Authorization") String token,
            @RequestParam double amount) {

        String email = jwtService.extractUsername(token.replace("Bearer ", ""));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        walletService.addFunds(user.getId(), amount);

        return new ApiResponseDto(true, "Money added successfully");
    }

    // ================= INVEST =================
    @PostMapping("/invest")
    public ApiResponseDto invest(
            @RequestHeader("Authorization") String token,
            @RequestParam double amount) {

        User user = getUserFromToken(token);

        walletService.invest(user.getId(), amount);
        return new ApiResponseDto(true, "Investment successful");
    }

    // ================= WITHDRAW =================
    @PostMapping("/withdraw")
    public ApiResponseDto withdraw(
            @RequestHeader("Authorization") String token,
            @RequestParam double amount) {

        String email = jwtService.extractUsername(token.replace("Bearer ", ""));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        walletService.withdraw(user.getId(), amount);

        return new ApiResponseDto(true, "Withdraw successful");
    }


    // ================= TRANSACTIONS =================
    @GetMapping("/transactions")
    public List<WalletTransactionDto> getTransactions(
            @RequestHeader("Authorization") String token) {

        User user = getUserFromToken(token);

        return walletService.getTransactions(user.getId());
    }
}

