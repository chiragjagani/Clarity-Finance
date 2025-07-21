package com.cj.financeapp.dto;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseUpdateDTO {
    @DecimalMin("0.01")
    public BigDecimal amount;
    public String category;
    public LocalDate expenseDate;
    public String note;
}
