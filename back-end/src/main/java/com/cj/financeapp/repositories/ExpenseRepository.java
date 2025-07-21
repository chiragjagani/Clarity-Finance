package com.cj.financeapp.repositories;

import com.cj.financeapp.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByUserId(Long userId);

    // Optional: Filter by category and date range
    List<Expense> findByUserIdAndCategoryAndExpenseDateBetween(Long userId, String category, LocalDate from, LocalDate to);

    List<Expense> findByUserIdAndExpenseDateBetween(Long userId, LocalDate from, LocalDate to);

    List<Expense> findByUserIdAndCategory(Long userId, String category);

    // For getting most expensive item, most spent category, etc you will likely add @Query methods.
}
