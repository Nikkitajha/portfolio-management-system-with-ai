package com.internshiproject.portfolioManagement.dto;

import lombok.Data;

@Data
public class AdminDashboardDto {

    private Long userId;
    private String email;

    private double totalInvestment;
    private double totalCurrentValue;
    private double profitLoss;


}
