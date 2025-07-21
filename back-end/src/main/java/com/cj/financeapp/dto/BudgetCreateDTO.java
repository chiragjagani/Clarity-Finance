package com.cj.financeapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BudgetCreateDTO {
    @Min(1) @Max(12)
    public int month;
    @Min(2000)
    public int year;
    @NotNull
    @DecimalMin("0.01")
    public BigDecimal budgetAmount;
    @DecimalMin("0.00")
    public BigDecimal rentAmount;
}
