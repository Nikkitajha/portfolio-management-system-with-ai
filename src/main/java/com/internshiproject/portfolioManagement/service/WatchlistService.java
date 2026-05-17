package com.internshiproject.portfolioManagement.service;

import com.internshiproject.portfolioManagement.entity.User;
import com.internshiproject.portfolioManagement.entity.Watchlist;
import com.internshiproject.portfolioManagement.repository.UserRepository;
import com.internshiproject.portfolioManagement.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
@Service
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private UserRepository userRepository;

    // GET WATCHLIST + UPDATE PRICE
    public List<Watchlist> getUserWatchlist(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Watchlist> list = watchlistRepository.findByUser(user);

        for (Watchlist stock : list) {

            double price = stock.getCurrentPrice();

            // First time price
            if (price == 0) {
                price = 100 + (Math.random() * 500);
            } else {
                // Small fluctuation (realistic)
                price = price * (1 + (Math.random() * 0.02 - 0.01));
            }

            stock.setCurrentPrice(price);
        }

        watchlistRepository.saveAll(list); //very important

        return list;
    }

    // ADD STOCK
    public void addStock(String email, Watchlist request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean exists = watchlistRepository
                .existsByUserAndSymbol(user, request.getSymbol());
        if (exists) return;

        Watchlist stock = new Watchlist();
        stock.setSymbol(request.getSymbol());
        stock.setCurrentPrice(0);
        stock.setUser(user);

        watchlistRepository.save(stock);
    }

    // REMOVE STOCK
    @Transactional
    public void removeStock(String email, String symbol) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        watchlistRepository.deleteByUserAndSymbol(user, symbol);
    }



    @Transactional
    public String toggleStock(String email, Watchlist request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean exists = watchlistRepository
                .existsByUserAndSymbol(user, request.getSymbol());

        if (exists) {
            watchlistRepository.deleteByUserAndSymbol(user, request.getSymbol());
            return "removed";
        } else {
            Watchlist stock = new Watchlist();
            stock.setSymbol(request.getSymbol());
            stock.setCurrentPrice(0);
            stock.setUser(user);

            watchlistRepository.save(stock);
            return "added";
        }
    }


    // ADMIN → get watchlist using userId
    public List<Watchlist> getUserWatchlistByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Watchlist> list = watchlistRepository.findByUser(user);

        for (Watchlist stock : list) {

            double price = stock.getCurrentPrice();

            if (price == 0) {
                price = 100 + (Math.random() * 500);
            } else {
                price = price * (1 + (Math.random() * 0.02 - 0.01));
            }

            stock.setCurrentPrice(price);
        }

        watchlistRepository.saveAll(list);

        return list;
    }
}

