package com.internshiproject.portfolioManagement.repository;


import com.internshiproject.portfolioManagement.entity.Transaction;
import com.internshiproject.portfolioManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser(User user);

}
