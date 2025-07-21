package com.cj.financeapp.services;

import com.cj.financeapp.dto.AiTipsDTO;
import com.cj.financeapp.repositories.*;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

// For production, use a real OpenAI API integration!
@Service
@RequiredArgsConstructor
public class AiTipsServiceImpl implements AiTipsService {

    private final ExpenseRepository expenseRepo;
    private final BudgetRepository budgetRepo;
    // Add WebClient/OpenAI service as needed.

    @Override
    public AiTipsDTO generateTips(Long userId) {
        // In production: gather expense stats, call OpenAI API, or rule-based tip generation.
        AiTipsDTO dto = new AiTipsDTO();
        dto.tips = new String[] {
                "You spent 20% of your budget on travel this month.",
                "Try to keep food expenses below 25% of your budget.",
                "Consider reviewing your shopping expenses."
        };
        return dto;
    }
}