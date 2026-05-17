package com.internshiproject.portfolioManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stocks")

public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol; // e.g. TCS, INFY
    private int quantity; // e.g. 1000

    private double entryPrice;  // buy price
    private double currentPrice;  // live price

    private LocalDate entryDate; // buy date

    private Double cap;

    private Double stopLoss;
    private String sector; // IT, BANKING, FMCG

    // tracking fields
    private Double lastAlertPercent;
    private Double highestPercent; // for trailing profit
    private LocalDateTime lastAlertDate;

    // alert flags (optional but useful)
    private Boolean stopLossTriggered = false;



    // relation with user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        if (this.entryDate == null) {
            this.entryDate = LocalDate.now();
        }
    }


}
