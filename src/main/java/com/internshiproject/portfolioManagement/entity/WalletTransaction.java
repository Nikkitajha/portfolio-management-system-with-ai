package com.internshiproject.portfolioManagement.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private String type; // ADD, INVEST, WITHDRAW, SELL

    private LocalDateTime dateTime;

    @ManyToOne
    @JsonIgnore
    private Wallet wallet;

}
