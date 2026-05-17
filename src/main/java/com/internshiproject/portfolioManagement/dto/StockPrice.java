package com.internshiproject.portfolioManagement.dto;

import lombok.Data;

@Data
public class StockPrice {
    private String symbol;
    private String companyName;
    private Double lastPrice;
    private Double change;
    private Double pChange;       // % change
    private Double open;
    private Double high;
    private Double low;
    private Double previousClose;
    private Long totalTradedVolume;
    private String lastUpdateTime;
}