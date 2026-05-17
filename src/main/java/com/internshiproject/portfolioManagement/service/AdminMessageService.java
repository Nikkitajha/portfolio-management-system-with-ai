package com.internshiproject.portfolioManagement.service;

import com.internshiproject.portfolioManagement.entity.AdminMessage;
import com.internshiproject.portfolioManagement.entity.User;
import com.internshiproject.portfolioManagement.repository.AdminMessageRepository;
import com.internshiproject.portfolioManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminMessageService {
    @Autowired
    private AdminMessageRepository adminMessageRepository;

    @Autowired
    private UserRepository userRepository;

    //  ADMIN SEND MESSAGE
    public void sendMessage(Long userId, String msg) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AdminMessage message = new AdminMessage();
        message.setMessage(msg);
        message.setDateTime(LocalDateTime.now());
        message.setUser(user);

        adminMessageRepository.save(message);
    }

    // USER GET MESSAGES
    public List<AdminMessage> getUserMessages(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return adminMessageRepository.findByUser(user);
    }

    public List<AdminMessage> getAllMessages() {
        return adminMessageRepository.findAll();
    }

}
