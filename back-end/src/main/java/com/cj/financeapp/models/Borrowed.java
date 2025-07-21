package com.cj.financeapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrowed")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Borrowed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable=false)
    private String lenderName;

    @Column(nullable=false)
    private String lenderEmail;

    @Column(nullable=false)
    private BigDecimal amount;

    @Column(nullable=false)
    private LocalDate borrowDate;

    private String reason;

    @Column(nullable=false)
    private boolean paidBack = false;
    private LocalDate paidBackDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
