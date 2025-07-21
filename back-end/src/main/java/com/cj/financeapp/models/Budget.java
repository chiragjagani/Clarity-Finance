package com.cj.financeapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="budgets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "month", "year"})
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable=false)
    private int month;

    @Column(nullable=false)
    private int year;

    @Column(nullable=false)
    private BigDecimal budgetAmount;

    @Column
    private BigDecimal rentAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
