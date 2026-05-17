package com.internshiproject.portfolioManagement.repository;

import com.internshiproject.portfolioManagement.entity.Portfolio;
import com.internshiproject.portfolioManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findByUser(User user);

    Portfolio findByUserAndSymbol(User user, String symbol);
    List<Portfolio> findByUserId(Long userId);
}
