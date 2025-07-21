package com.cj.financeapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BorrowedCreateDTO {
    @NotBlank
    public String lenderName;
    @Email
    @NotBlank
    public String lenderEmail;
    @NotNull @DecimalMin("0.01")
    public BigDecimal amount;
    @NotNull
    public LocalDate borrowDate;
    public String reason;
}
