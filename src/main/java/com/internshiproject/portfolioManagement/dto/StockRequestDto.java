package com.internshiproject.portfolioManagement.dto;

import lombok.Data;

@Data
public class StockRequestDto {
    private String symbol;
    private int quantity;
    private double entryPrice;
    private Double cap;
    private String sector;
}
