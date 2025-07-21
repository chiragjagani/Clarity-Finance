package com.cj.financeapp.controllers;

import com.cj.financeapp.dto.*;
import com.cj.financeapp.security.UserPrincipal;
import com.cj.financeapp.services.LentService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/lent")
@Slf4j
public class LentController {

    @Autowired
    private LentService lentService;

    @PostMapping
    public ResponseEntity<LentResponseDTO> createLent(
            @Valid @RequestBody LentCreateDTO dto, Principal principal
    ) {
        log.debug("📥 Lent create request received: {}", dto);
        Long userId = getUserIdFromPrincipal(principal);
        log.debug("🔍 Extracted userId: {}", userId);
        LentResponseDTO resp = lentService.createLent(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/{id}/remind")
    public ResponseEntity<?> sendReminder(
            @PathVariable Long id, Principal principal
    ) {
        log.debug("📤 Reminder requested for Lent ID: {}", id);
        Long userId = getUserIdFromPrincipal(principal);
        lentService.sendReminder(id, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/paid")
    public ResponseEntity<LentResponseDTO> markRepaid(
            @PathVariable Long id,
            @RequestBody LentMarkPaidDTO dto,
            Principal principal
    ) {
        log.debug("💰 Marking Lent ID {} as repaid: {}", id, dto);
        Long userId = getUserIdFromPrincipal(principal);
        LentResponseDTO resp = lentService.markAsRepaid(id, userId, dto);
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<List<LentResponseDTO>> listLent(Principal principal) {
        log.debug("📄 Listing lent entries");
        Long userId = getUserIdFromPrincipal(principal);
        List<LentResponseDTO> result = lentService.listLent(userId);
        return ResponseEntity.ok(result);
    }

    private Long getUserIdFromPrincipal(Principal principal) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            return userPrincipal.getId();
        } catch (Exception e) {
            log.error("❌ Principal name is not a valid user ID (expected Long): {}", principal.getName());
            throw new IllegalArgumentException("Invalid user principal: " + principal.getName());
        }
    }
}
