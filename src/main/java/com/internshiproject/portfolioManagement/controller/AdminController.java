package com.internshiproject.portfolioManagement.controller;

import com.internshiproject.portfolioManagement.dto.AdminDashboardDto;
import com.internshiproject.portfolioManagement.dto.AdminDto;
import com.internshiproject.portfolioManagement.dto.PortfolioDto;
import com.internshiproject.portfolioManagement.entity.Portfolio;
import com.internshiproject.portfolioManagement.entity.Wallet;
import com.internshiproject.portfolioManagement.entity.Watchlist;
import com.internshiproject.portfolioManagement.service.AdminService;
import com.internshiproject.portfolioManagement.service.PortfolioService;
import com.internshiproject.portfolioManagement.service.UserService;
import com.internshiproject.portfolioManagement.service.WalletService;
import com.internshiproject.portfolioManagement.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private WalletService walletService;

    // Get all users
    @GetMapping("/users")
    public List<AdminDashboardDto> getUsers() {
        return adminService.getAllUsersWithStats();
    }
    //Get user portfolio
    @GetMapping("/user/{id}/portfolio")
    public List<PortfolioDto> getUserPortfolio(@PathVariable Long id) {

        List<Portfolio> portfolioList = portfolioService.getPortfolioByUserId(id);

        return portfolioList.stream()
                .map(p -> new PortfolioDto(
                        p.getSymbol(),
                        p.getQuantity(),
                        p.getEntryPrice(),
                        p.getCurrentPrice()
                ))
                .toList();
    }

    // Get user watchlist
    @GetMapping("/user/{id}/watchlist")
    public List<Watchlist> getUserWatchlist(@PathVariable Long id) {
        return watchlistService.getUserWatchlistByUserId(id);
    }

    @GetMapping("/user/{id}/dashboard")
    public ResponseEntity<?> getDashboard(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getDashboard(id));
    }

    // Get user history
    @GetMapping("/user/{id}/history")
    public ResponseEntity<?> getUserHistory(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserHistory(id));
    }

    // Get user wallet
    @GetMapping("/user/{id}/wallet")
    public Wallet getUserWallet(@PathVariable Long id) {
        return walletService.getWallet(id);
    }


}
