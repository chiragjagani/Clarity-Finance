package com.cj.financeapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable=false)
    @DecimalMin("0.01")
    private BigDecimal amount;

    @Column(nullable=false, length=50)
    private String category;

    @Column(nullable=false)
    private LocalDate expenseDate;

    @Column
    private String note;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
