package com.internshiproject.portfolioManagement.controller;

import com.internshiproject.portfolioManagement.dto.StockRequestDto;
import com.internshiproject.portfolioManagement.entity.Stock;
import com.internshiproject.portfolioManagement.service.JwtService;
import com.internshiproject.portfolioManagement.service.PortfolioService;
import com.internshiproject.portfolioManagement.entity.Transaction;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin("*")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private JwtService jwtService;

    // ADD STOCK
    @PostMapping("/add")
    public ResponseEntity<?> addStock(
            @RequestHeader("Authorization") String token,
            @RequestBody StockRequestDto request
    ) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));

        portfolioService.addStock(email, request);

        return ResponseEntity.ok("Stock added successfully");
    }

    // GET ALL STOCKS
    @GetMapping
    public ResponseEntity<List<Stock>> getPortfolio(
            @RequestHeader("Authorization") String token
    ) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));

        return ResponseEntity.ok(portfolioService.getUserStocks(email));
    }

    //  SELL STOCK
    @PostMapping("/sell/{id}")
    public ResponseEntity<?> sellStock(
            @PathVariable Long id,
            @RequestParam int quantity
    ) {
        portfolioService.sellStock(id, quantity);

        return ResponseEntity.ok("Stock sold successfully");
    }

    //  GET HISTORY
    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getHistory(
            @RequestHeader("Authorization") String token
    ) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));

        return ResponseEntity.ok(portfolioService.getHistory(email));
    }

    @GetMapping("/total")
    public double getTotal(Authentication auth) {
        String email = auth.getName();
        return portfolioService.getTotalProfitLoss(email);
    }



}