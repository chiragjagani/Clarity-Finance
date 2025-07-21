package com.cj.financeapp.services;

import com.cj.financeapp.dto.DashboardSummaryDTO;
import com.cj.financeapp.models.*;
import com.cj.financeapp.repositories.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final ExpenseRepository expenseRepo;
    private final BudgetRepository budgetRepo;
    private final BorrowedRepository borrowedRepo;
    private final LentRepository lentRepo;

    @Override
    public DashboardSummaryDTO getDashboardSummary(Long userId) {
        log.info("📊 Generating dashboard summary for userId: {}", userId);

        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        // Budget and rent
        Budget budget = budgetRepo.findByUserIdAndMonthAndYear(userId, month, year).orElse(null);
        BigDecimal budgetTotal = budget != null ? budget.getBudgetAmount() : BigDecimal.ZERO;
        BigDecimal rent = budget != null && budget.getRentAmount() != null ? budget.getRentAmount() : BigDecimal.ZERO;

        log.debug("📅 Current Month-Year: {}/{}", month, year);
        log.debug("💰 Retrieved budget: {} | Rent: {}", budgetTotal, rent);

        // Expenses
        List<Expense> expenses = expenseRepo.findAllByUserId(userId);
        log.debug("🧾 Total expenses found: {}", expenses.size());

        BigDecimal spent = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal afterRent = budgetTotal.subtract(rent);

        String mostSpentCategory = expenses.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Expense::getCategory,
                        java.util.stream.Collectors.summingDouble(e -> e.getAmount().doubleValue())))
                .entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse(null);

        Expense expensiveItem = expenses.stream()
                .max(Comparator.comparing(Expense::getAmount))
                .orElse(null);

        log.debug("💸 Total Spent: {} | Remaining After Rent: {}", spent, afterRent);
        log.debug("📂 Most Spent Category: {} | Highest Expense Note: {}",
                mostSpentCategory,
                expensiveItem != null ? expensiveItem.getNote() : "N/A");

        // Borrowed and Lent
        BigDecimal borrowed = borrowedRepo.findAllByUserId(userId).stream()
                .map(Borrowed::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal lent = lentRepo.findAllByUserId(userId).stream()
                .map(Lent::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.debug("📈 Borrowed: {} | Lent: {}", borrowed, lent);

        // Prepare DTO
        DashboardSummaryDTO dto = new DashboardSummaryDTO();
        dto.totalBudget = budgetTotal;
        dto.totalSpent = spent;
        dto.remainingAfterRent = afterRent;
        dto.mostSpentCategory = mostSpentCategory;
        dto.mostExpensiveItem = expensiveItem != null ? expensiveItem.getNote() : null;
        dto.totalBorrowed = borrowed;
        dto.totalLent = lent;

        log.info("✅ Dashboard summary created successfully for userId: {}", userId);
        return dto;
    }
}
