package com.internshiproject.portfolioManagement.controller;

import com.internshiproject.portfolioManagement.entity.AdminMessage;
import com.internshiproject.portfolioManagement.entity.User;
import com.internshiproject.portfolioManagement.service.AdminMessageService;
import com.internshiproject.portfolioManagement.service.JwtService;
import com.internshiproject.portfolioManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin-message")
@CrossOrigin
public class AdminMessageController {
    @Autowired
    private AdminMessageService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    // ================= COMMON METHOD =================
    private User getUserFromToken(String token) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //  ADMIN SEND
    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestParam Long userId,
                                       @RequestParam String message) {

        service.sendMessage(userId, message);
        return ResponseEntity.ok("Message sent successfully");
    }

    //  USER FETCH
    @GetMapping("/user")
    public List<AdminMessage> getMessages(@RequestParam( value = "userId", required = false) Long userId,
                                       @RequestHeader("Authorization") String authorization) {
        // If userId is not provided, get it from JWT token
        if (userId == null) {
            User currentUser = getUserFromToken(authorization);
            return service.getUserMessages(currentUser.getId());
        }
        return service.getUserMessages(userId);
    }
    
    //  GET ALL ADMIN MESSAGES
    @GetMapping("/all")
    public List<AdminMessage> getAllMessages() {
        return service.getAllMessages();
    }
    
    //  GET CURRENT USER INFO
    @GetMapping("/current-user")
    public User getCurrentUser(@RequestHeader("Authorization") String token) {
        return getUserFromToken(token);
    }
}
