package com.internshiproject.portfolioManagement.service;

import com.internshiproject.portfolioManagement.dto.AdminDto;
import com.internshiproject.portfolioManagement.dto.RegisterDto;
import com.internshiproject.portfolioManagement.entity.User;
import com.internshiproject.portfolioManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // REGISTER
    public void registerUser(RegisterDto dto) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        String otp = generateOtp();

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setOtp(otp);
        user.setVerified(false);

        userRepository.save(user);

        // send OTP email
        emailService.sendOtp(dto.getEmail(), otp);
    }

    //  SEND OTP (LOGIN STEP 1)
    public String sendOtp(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = generateOtp();

        user.setOtp(otp);
        userRepository.save(user);

        emailService.sendOtp(email, otp);

        return "OTP sent to your email";
    }

    //  VERIFY + LOGIN (LOGIN STEP 2)
    public String verifyLogin(String email, String password, String otp) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // check password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // check OTP
        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // mark verified
        user.setVerified(true);
        user.setOtp(null);
        userRepository.save(user);

        // generate JWT
        return jwtService.generateToken(user.getEmail());
    }

    //  OTP GENERATOR
    private String generateOtp() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    public List<AdminDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new AdminDto(
                        user.getId(),
                        user.getEmail()
                ))
                .toList();
    }
}