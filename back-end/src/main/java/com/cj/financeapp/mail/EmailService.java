package com.cj.financeapp.mail;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface EmailService {
    void sendBorrowedRepaidEmail(String to, String myName, BigDecimal amount);
    void sendLentReminderEmail(String to, String myName, BigDecimal amount, LocalDate dueDate);
}