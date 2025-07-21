package com.cj.financeapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "lent")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable=false)
    private String borrowerName;

    @Column(nullable=false)
    private String borrowerEmail;

    @Column(nullable=false)
    private BigDecimal amount;

    @Column(nullable=false)
    private LocalDate lendDate;

    private String reason;

    private LocalDate dueDate;

    @Column(nullable=false)
    private boolean repaid = false;
    private LocalDate repaidDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
