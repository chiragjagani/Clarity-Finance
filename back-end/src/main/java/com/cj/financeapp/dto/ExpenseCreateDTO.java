package com.cj.financeapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseCreateDTO {
    @NotNull
    @DecimalMin("0.01")
    public BigDecimal amount;
    @NotBlank
    public String category;
    @NotNull
    public LocalDate expenseDate;
    public String note;
}
