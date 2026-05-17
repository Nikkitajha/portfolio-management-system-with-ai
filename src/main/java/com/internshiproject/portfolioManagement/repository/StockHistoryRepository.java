package com.internshiproject.portfolioManagement.repository;

import com.internshiproject.portfolioManagement.entity.StockHistory;
import com.internshiproject.portfolioManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {

    List<StockHistory> findByUser(User user);
}
