package com.internshiproject.portfolioManagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private int quantity;

    private double entryPrice;//buy price
    private Double sellPrice;// only for SELL
    private Double profitLoss;//only for sell

    private String type; // BUY or SELL

    private LocalDateTime entryDate; // for BUY
    private LocalDateTime sellDate;  // for SELL


    //private LocalDateTime createdAt;

    @ManyToOne
    private User user;

    //auto set date
    @PrePersist
    public void prePersist() {
        if (this.type.equals("BUY")) {
            this.entryDate = LocalDateTime.now();
        }

    }
}
