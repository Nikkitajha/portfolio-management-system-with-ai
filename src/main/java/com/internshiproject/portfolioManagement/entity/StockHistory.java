package com.internshiproject.portfolioManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stock_history")
public class StockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private int quantity;

    private double buyPrice;
    private double sellPrice;

    private LocalDate buyDate;
    private LocalDate sellDate;

    private double profitOrLoss;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
