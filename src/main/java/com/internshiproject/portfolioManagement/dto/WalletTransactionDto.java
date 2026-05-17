package com.internshiproject.portfolioManagement.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WalletTransactionDto {

        private Long id;
        private double amount;
        private String type;
        private LocalDateTime dateTime;

        public WalletTransactionDto(Long id, double amount, String type, LocalDateTime dateTime) {
            this.id = id;
            this.amount = amount;
            this.type = type;
            this.dateTime = dateTime;
        }
    }

