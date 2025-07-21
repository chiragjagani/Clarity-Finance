package com.cj.financeapp.dto;

import java.math.BigDecimal;

public class DashboardSummaryDTO {
    public BigDecimal totalBudget;
    public BigDecimal totalSpent;
    public BigDecimal remainingAfterRent;
    public String mostSpentCategory;
    public String mostExpensiveItem;
    public BigDecimal totalBorrowed;
    public BigDecimal totalLent;
}
