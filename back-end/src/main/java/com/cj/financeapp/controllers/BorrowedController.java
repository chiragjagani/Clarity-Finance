package com.cj.financeapp.controllers;

import com.cj.financeapp.dto.*;
import com.cj.financeapp.security.UserPrincipal;
import com.cj.financeapp.services.BorrowedService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowed")
@Slf4j
public class BorrowedController {

    @Autowired
    private BorrowedService borrowedService;

    /* ──────────────────────────────────────────────────────────
       POST /api/borrowed  →  create a new borrowed record
       ────────────────────────────────────────────────────────── */
    @PostMapping
    public ResponseEntity<BorrowedResponseDTO> createBorrowed(
            @Valid @RequestBody BorrowedCreateDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        log.debug("📥 Borrowed create request: {}", dto);
        Long userId = userPrincipal.getId();
        log.info("Creating borrowed record for userId: {}", userId);

        BorrowedResponseDTO resp = borrowedService.createBorrowed(userId, dto);
        log.info("✅ Borrowed record created: {}", resp);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    /* ──────────────────────────────────────────────────────────
       PATCH /api/borrowed/{id}/paid  →  mark as paid back
       ────────────────────────────────────────────────────────── */
    @PatchMapping("/{id}/paid")
    public ResponseEntity<BorrowedResponseDTO> markPaidBack(
            @PathVariable Long id,
            @RequestBody BorrowedMarkPaidDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        log.debug("✏️ Mark paid request for borrowedId: {}, payload: {}", id, dto);
        Long userId = userPrincipal.getId();
        log.info("Marking borrowedId: {} as paid for userId: {}", id, userId);

        BorrowedResponseDTO resp = borrowedService.markAsPaidBack(id, userId, dto);
        log.info("✅ Borrowed record updated: {}", resp);
        return ResponseEntity.ok(resp);
    }

    /* ──────────────────────────────────────────────────────────
       GET /api/borrowed  →  list all borrowed records
       ────────────────────────────────────────────────────────── */
    @GetMapping
    public ResponseEntity<List<BorrowedResponseDTO>> listBorrowed(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Long userId = userPrincipal.getId();
        log.info("📊 Listing borrowed records for userId: {}", userId);

        List<BorrowedResponseDTO> result = borrowedService.listBorrowed(userId);
        log.debug("📤 Returned {} borrowed records", result.size());
        return ResponseEntity.ok(result);
    }
}
