package com.cj.financeapp.repositories;

import com.cj.financeapp.models.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    // Get budget for specific month and year
    Optional<Budget> findByUserIdAndMonthAndYear(Long userId, int month, int year);

    List<Budget> findAllByUserId(Long userId);
}
