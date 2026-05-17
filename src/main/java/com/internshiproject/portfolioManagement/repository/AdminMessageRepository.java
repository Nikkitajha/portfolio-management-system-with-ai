package com.internshiproject.portfolioManagement.repository;


import com.internshiproject.portfolioManagement.entity.AdminMessage;
import com.internshiproject.portfolioManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminMessageRepository extends JpaRepository<AdminMessage, Long> {
    List<AdminMessage> findByUser(User user);
}
