package com.internshiproject.portfolioManagement.service;

import com.internshiproject.portfolioManagement.dto.WalletTransactionDto;
import com.internshiproject.portfolioManagement.entity.User;
import com.internshiproject.portfolioManagement.entity.Wallet;
import com.internshiproject.portfolioManagement.entity.WalletTransaction;
import com.internshiproject.portfolioManagement.repository.UserRepository;
import com.internshiproject.portfolioManagement.repository.WalletRepository;
import com.internshiproject.portfolioManagement.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletTransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    // ================= ADD MONEY =================
    public void addFunds(Long userId, double amount) {

        Wallet wallet = getOrCreateWallet(userId);

        wallet.setRemainingAmount(wallet.getRemainingAmount() + amount);

        WalletTransaction tx = new WalletTransaction();
        tx.setAmount(amount);
        tx.setType("ADD");
        tx.setDateTime(LocalDateTime.now());
        tx.setWallet(wallet);

        walletRepository.save(wallet);
        transactionRepository.save(tx);
    }

    // ================= INVEST (BUY STOCK) =================
    public void invest(Long userId, double amount) {

        Wallet wallet = getOrCreateWallet(userId);

        if (wallet.getRemainingAmount() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        wallet.setRemainingAmount(wallet.getRemainingAmount() - amount);
        wallet.setInvestedAmount(wallet.getInvestedAmount() + amount);

        WalletTransaction tx = new WalletTransaction();
        tx.setAmount(amount);
        tx.setType("INVEST");
        tx.setDateTime(LocalDateTime.now());
        tx.setWallet(wallet);

        walletRepository.save(wallet);
        transactionRepository.save(tx);
    }

    // ================= WITHDRAW =================
    public void withdraw(Long userId, double amount) {

        Wallet wallet = getOrCreateWallet(userId);

        if (wallet.getRemainingAmount() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        wallet.setRemainingAmount(wallet.getRemainingAmount() - amount);

        WalletTransaction tx = new WalletTransaction();
        tx.setAmount(amount);
        tx.setType("WITHDRAW");
        tx.setDateTime(LocalDateTime.now());
        tx.setWallet(wallet);

        walletRepository.save(wallet);
        transactionRepository.save(tx);
    }

    // ================= GET WALLET DETAILS =================
    public Wallet getWallet(Long userId) {

        Wallet wallet = walletRepository.findByUserId(userId);

        if (wallet == null) {
            return getOrCreateWallet(userId); // auto-create if not exists
        }

        return wallet;
    }

    // ================= GET TRANSACTIONS =================
    public List<WalletTransactionDto> getTransactions(Long userId) {

        Wallet wallet = getOrCreateWallet(userId); // FIXED (no null error)

        List<WalletTransaction> transactions =
                transactionRepository.findByWalletOrderByDateTimeDesc(wallet);

        return transactions.stream()
                .map(tx -> new WalletTransactionDto(
                        tx.getId(),
                        tx.getAmount(),
                        tx.getType(),
                        tx.getDateTime()
                ))
                .toList();
    }

    // ================= CREATE WALLET IF NOT EXISTS =================
    public Wallet getOrCreateWallet(Long userId) {

        Wallet wallet = walletRepository.findByUserId(userId);

        if (wallet == null) {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            wallet = new Wallet();
            wallet.setUser(user);
            wallet.setRemainingAmount(0);
            wallet.setInvestedAmount(0);

            walletRepository.save(wallet);
        }

        return wallet;
    }
}