package com.internshiproject.portfolioManagement.controller;

import com.internshiproject.portfolioManagement.entity.User;
import com.internshiproject.portfolioManagement.entity.Watchlist;
import com.internshiproject.portfolioManagement.service.JwtService;
import com.internshiproject.portfolioManagement.service.WatchlistService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@CrossOrigin("*")
public class WatchlistController {

        @Autowired
        private WatchlistService watchlistService;

        @Autowired
        private JwtService jwtService;

        @GetMapping
        public List<Watchlist> getWatchlist(
                @RequestHeader("Authorization") String token
        ) {
            String email = jwtService.extractUsername(token.replace("Bearer ", ""));
            return watchlistService.getUserWatchlist(email);
        }

        @PostMapping("/add")
        public String addStock(
            @RequestHeader("Authorization") String token,
            @RequestBody Watchlist request
      ) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        watchlistService.addStock(email, request);
        return "Added";
     }

    @DeleteMapping("/remove")
    public ResponseEntity<?> remove(
            @RequestHeader("Authorization") String token,
            @RequestParam String symbol) {

        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        watchlistService.removeStock(email, symbol);

        return ResponseEntity.ok("Removed");
    }

    @PostMapping("/toggle")
    public String toggle(
            @RequestHeader("Authorization") String token,
            @RequestBody Watchlist request) {

        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        return watchlistService.toggleStock(email, request);
    }
}


