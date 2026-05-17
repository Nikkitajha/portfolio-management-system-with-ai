package com.internshiproject.portfolioManagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Portfolio {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String symbol;
        private int quantity;
        private double entryPrice;
        private double currentPrice;
        private Double cap;

        @ManyToOne
        private User user;
    }

