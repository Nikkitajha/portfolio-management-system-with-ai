package com.internshiproject.portfolioManagement.repository;

import com.internshiproject.portfolioManagement.entity.Stock;
import com.internshiproject.portfolioManagement.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findByUser(User user);

    Stock findByUserAndSymbol(
            User user,
            String symbol
    );

    Optional<Stock> findByIdAndUser(
            Long id,
            User user
    );
}
