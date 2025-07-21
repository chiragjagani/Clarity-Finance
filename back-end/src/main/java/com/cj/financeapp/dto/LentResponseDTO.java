package com.cj.financeapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LentResponseDTO {
    public Long id;
    public String borrowerName;
    public String borrowerEmail;
    public BigDecimal amount;
    public LocalDate lendDate;
    public String reason;
    public LocalDate dueDate;
    public boolean repaid;
    public LocalDate repaidDate;
}
