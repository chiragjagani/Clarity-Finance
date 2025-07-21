package com.cj.financeapp.controllers;

import com.cj.financeapp.dto.*;
import com.cj.financeapp.security.UserPrincipal;
import com.cj.financeapp.services.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@Validated
@Slf4j
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(
            @Valid @RequestBody ExpenseCreateDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        log.debug("📥 Create expense request: {}", dto);
        Long userId = getUserIdFromPrincipal(userPrincipal);
        log.info("Creating expense for user ID: {}", userId);

        ExpenseResponseDTO resp = expenseService.createExpense(dto, userId);
        log.info("✅ Expense created: {}", resp);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Long userId = getUserIdFromPrincipal(userPrincipal);
        log.info("📊 Fetching expenses for user ID: {} with category={}, from={}, to={}", userId, category, from, to);

        List<ExpenseResponseDTO> expenses = expenseService.getExpenses(userId, category, from, to);

        log.debug("📤 Returned {} expenses", expenses.size());
        return ResponseEntity.ok(expenses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseUpdateDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        log.debug("✏️ Update request for expense ID: {}, payload: {}", id, dto);
        Long userId = getUserIdFromPrincipal(userPrincipal);
        log.info("Updating expense ID: {} for user ID: {}", id, userId);

        ExpenseResponseDTO resp = expenseService.updateExpense(id, dto, userId);
        log.info("✅ Expense updated successfully: {}", resp);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        log.info("🗑️ Deleting expense ID: {}", id);
        Long userId = getUserIdFromPrincipal(userPrincipal);
        expenseService.deleteExpense(id, userId);
        log.info("✅ Expense deleted for ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    private Long getUserIdFromPrincipal(UserPrincipal principal) {
        Long userId = principal.getId();
        log.debug("🔍 Extracted user ID from principal: {}", userId);
        return userId;
    }
}
