package com.cj.financeapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseResponseDTO {public Long id;
    public BigDecimal amount;
    public String category;
    public LocalDate expenseDate;
    public String note;
}
