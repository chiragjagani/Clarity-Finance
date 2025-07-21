package com.cj.financeapp.services;

import com.cj.financeapp.dto.*;
import com.cj.financeapp.models.*;
import com.cj.financeapp.repositories.*;
import com.cj.financeapp.exceptions.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepo;
    private final UserRepository userRepo;

    @Override
    public BudgetResponseDTO setBudget(Long userId, BudgetCreateDTO dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Budget budget = budgetRepo.findByUserIdAndMonthAndYear(userId, dto.month, dto.year)
                .orElse(Budget.builder().user(user).month(dto.month).year(dto.year).build());
        budget.setBudgetAmount(dto.budgetAmount);
        budget.setRentAmount(dto.rentAmount);

        Budget saved = budgetRepo.save(budget);

        BudgetResponseDTO resp = new BudgetResponseDTO();
        resp.month = saved.getMonth();
        resp.year = saved.getYear();
        resp.budgetAmount = saved.getBudgetAmount();
        resp.rentAmount = saved.getRentAmount();
        return resp;
    }

    @Override
    public BudgetResponseDTO getCurrentBudget(Long userId) {
        LocalDate now = LocalDate.now();
        Budget budget = budgetRepo.findByUserIdAndMonthAndYear(
                        userId, now.getMonthValue(), now.getYear())
                .orElseThrow(() -> new ResourceNotFoundException("Budget", "month/year", now.getMonthValue() + "/" + now.getYear()));

        BudgetResponseDTO resp = new BudgetResponseDTO();
        resp.month = budget.getMonth();
        resp.year = budget.getYear();
        resp.budgetAmount = budget.getBudgetAmount();
        resp.rentAmount = budget.getRentAmount();
        return resp;
    }
}