package com.cj.financeapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50)
    private String name;

    @Column(nullable=false, unique=true, length=100)
    private String email;

    @Column(nullable=false)
    private String passwordHash;

    @Column(length = 20)
    private String role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
