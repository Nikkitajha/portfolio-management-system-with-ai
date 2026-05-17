package com.internshiproject.portfolioManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PortfolioDto {
    private String symbol;
    private int quantity;
    private double entryPrice;
    private double currentPrice;
}


