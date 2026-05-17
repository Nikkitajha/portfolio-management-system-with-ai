package com.internshiproject.portfolioManagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private LocalDateTime dateTime;

//    private boolean isRead;

    //  which user this message is for
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
