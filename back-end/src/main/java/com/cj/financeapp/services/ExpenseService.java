package com.cj.financeapp.services;

import com.cj.financeapp.dto.ExpenseCreateDTO;
import com.cj.financeapp.dto.ExpenseResponseDTO;
import com.cj.financeapp.dto.ExpenseUpdateDTO;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    ExpenseResponseDTO createExpense(ExpenseCreateDTO dto, Long userId);

    List<ExpenseResponseDTO> getExpenses(Long userId, String category, LocalDate from, LocalDate to);

    ExpenseResponseDTO updateExpense(Long id, ExpenseUpdateDTO dto, Long userId);

    void deleteExpense(Long id, Long userId);
}