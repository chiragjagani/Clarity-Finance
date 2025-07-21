package com.cj.financeapp.controllers;

import com.cj.financeapp.dto.AiTipsDTO;
import com.cj.financeapp.security.UserPrincipal;
import com.cj.financeapp.services.AiTipsService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@Slf4j
public class AiTipsController {

    @Autowired
    private AiTipsService aiTipsService;

    @GetMapping("/tips")
    public ResponseEntity<AiTipsDTO> getFinanceTips(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        log.debug("📥 AI Tips request received");
        log.debug("🔍 Authenticated user: {}", userPrincipal.getUsername());

        Long userId = userPrincipal.getId();
        log.info("Generating finance tips for userId: {}", userId);

        AiTipsDTO dto = aiTipsService.generateTips(userId);
        log.debug("✅ Tips generated successfully");

        return ResponseEntity.ok(dto);
    }
}
