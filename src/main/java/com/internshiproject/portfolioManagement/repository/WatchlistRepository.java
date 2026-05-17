package com.internshiproject.portfolioManagement.repository;

import com.internshiproject.portfolioManagement.entity.User;
import com.internshiproject.portfolioManagement.entity.Watchlist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    List<Watchlist> findByUser(User user);


    @Modifying
    @Transactional
    void deleteByUserAndSymbol(User user, String symbol);
     boolean existsByUserAndSymbol(User user, String symbol);

}
