package com.cj.financeapp.services;

import com.cj.financeapp.dto.BudgetCreateDTO;
import com.cj.financeapp.dto.BudgetResponseDTO;

public interface BudgetService {
    BudgetResponseDTO setBudget(Long userId, BudgetCreateDTO dto);
    BudgetResponseDTO getCurrentBudget(Long userId);
}