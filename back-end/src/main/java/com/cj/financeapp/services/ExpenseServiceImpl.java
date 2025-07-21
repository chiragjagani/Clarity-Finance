package com.cj.financeapp.services;

import com.cj.financeapp.dto.*;
import com.cj.financeapp.models.*;
import com.cj.financeapp.repositories.*;
import com.cj.financeapp.exceptions.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepo;
    private final UserRepository userRepo;

    @Override
    public ExpenseResponseDTO createExpense(ExpenseCreateDTO dto, Long userId) {
        log.info("Creating expense for user ID: {}", userId);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User", "id", userId);
                });

        Expense expense = Expense.builder()
                .user(user)
                .amount(dto.amount)
                .category(dto.category)
                .expenseDate(dto.expenseDate)
                .note(dto.note)
                .build();

        Expense saved = expenseRepo.save(expense);
        log.info("Expense created with ID: {} for user ID: {}", saved.getId(), userId);

        return mapToResponse(saved);
    }

    @Override
    public List<ExpenseResponseDTO> getExpenses(Long userId, String category, LocalDate from, LocalDate to) {
        List<Expense> list;

        if (category != null && from != null && to != null) {
            list = expenseRepo.findByUserIdAndCategoryAndExpenseDateBetween(userId, category, from, to);
        } else if (category != null) {
            list = expenseRepo.findByUserIdAndCategory(userId, category);
        } else if (from != null && to != null) {
            list = expenseRepo.findByUserIdAndExpenseDateBetween(userId, from, to);
        } else {
            list = expenseRepo.findAllByUserId(userId);
        }

        return list.stream().map(this::mapToResponse).toList();
    }


    @Override
    public ExpenseResponseDTO updateExpense(Long id, ExpenseUpdateDTO dto, Long userId) {
        log.info("Updating expense ID: {} for user ID: {}", id, userId);

        Expense expense = expenseRepo.findById(id).orElseThrow(() -> {
            log.warn("Expense not found with ID: {}", id);
            return new ResourceNotFoundException("Expense", "id", id);
        });

        if (!expense.getUser().getId().equals(userId)) {
            log.warn("Unauthorized update attempt on expense ID: {} by user ID: {}", id, userId);
            throw new UnauthorizedException("You do not own this expense");
        }

        if (dto.amount != null) expense.setAmount(dto.amount);
        if (dto.category != null) expense.setCategory(dto.category);
        if (dto.expenseDate != null) expense.setExpenseDate(dto.expenseDate);
        if (dto.note != null) expense.setNote(dto.note);

        Expense saved = expenseRepo.save(expense);
        log.info("Expense ID: {} updated successfully for user ID: {}", id, userId);

        return mapToResponse(saved);
    }

    @Override
    public void deleteExpense(Long id, Long userId) {
        log.info("Attempting to delete expense ID: {} for user ID: {}", id, userId);

        Expense expense = expenseRepo.findById(id).orElseThrow(() -> {
            log.warn("Expense not found with ID: {}", id);
            return new ResourceNotFoundException("Expense", "id", id);
        });

        if (!expense.getUser().getId().equals(userId)) {
            log.warn("Unauthorized delete attempt on expense ID: {} by user ID: {}", id, userId);
            throw new UnauthorizedException("You do not own this expense");
        }

        expenseRepo.deleteById(id);
        log.info("Expense ID: {} deleted successfully", id);
    }

    private ExpenseResponseDTO mapToResponse(Expense e) {
        return new ExpenseResponseDTO() {{
            id = e.getId();
            amount = e.getAmount();
            category = e.getCategory();
            expenseDate = e.getExpenseDate();
            note = e.getNote();
        }};
    }
}
