package com.cj.financeapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BorrowedResponseDTO {
    public Long id;
    public String lenderName;
    public String lenderEmail;
    public BigDecimal amount;
    public LocalDate borrowDate;
    public String reason;
    public boolean paidBack;
    public LocalDate paidBackDate;
}
