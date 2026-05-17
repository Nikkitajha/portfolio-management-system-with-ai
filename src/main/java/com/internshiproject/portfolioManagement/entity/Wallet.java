package com.internshiproject.portfolioManagement.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double investedAmount;
    private double remainingAmount;

    @OneToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

}
