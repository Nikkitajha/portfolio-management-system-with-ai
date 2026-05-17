package com.internshiproject.portfolioManagement.repository;

import com.internshiproject.portfolioManagement.entity.Wallet;
import com.internshiproject.portfolioManagement.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    List<WalletTransaction> findByWalletOrderByDateTimeDesc(Wallet wallet);
    List<WalletTransaction> findByWallet(Wallet wallet);
}
