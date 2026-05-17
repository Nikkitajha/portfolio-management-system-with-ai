package com.internshiproject.portfolioManagement.dto;

import lombok.Data;

@Data
public class RegisterDto {

    private String username;
    private String email;
    private String phone;
    private String password;
    private String confirmPassword;

}
