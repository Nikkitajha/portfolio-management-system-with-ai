package com.internshiproject.portfolioManagement.repository;

import com.internshiproject.portfolioManagement.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByUserId(Long userId);
}
