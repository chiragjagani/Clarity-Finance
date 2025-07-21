package com.cj.financeapp.controllers;

import com.cj.financeapp.dto.*;
import com.cj.financeapp.security.UserPrincipal;
import com.cj.financeapp.services.BudgetService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
@Validated
@Slf4j
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponseDTO> setBudget(
            @Valid @RequestBody BudgetCreateDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        log.debug("📥 Budget create request: {}", dto);
        Long userId = userPrincipal.getId();
        log.info("📌 Budget creation for userId: {}", userId);

        BudgetResponseDTO resp = budgetService.setBudget(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/current")
    public ResponseEntity<BudgetResponseDTO> getCurrentBudget(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Long userId = userPrincipal.getId();
        log.info("📌 Fetching current budget for userId: {}", userId);

        BudgetResponseDTO resp = budgetService.getCurrentBudget(userId);
        return ResponseEntity.ok(resp);
    }
}
