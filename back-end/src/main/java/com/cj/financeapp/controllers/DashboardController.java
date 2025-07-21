package com.cj.financeapp.controllers;

import com.cj.financeapp.dto.DashboardSummaryDTO;
import com.cj.financeapp.security.UserPrincipal;
import com.cj.financeapp.services.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@Slf4j
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        log.debug("📊 Dashboard summary requested for principal: {}", userPrincipal.getUsername());

        Long userId = userPrincipal.getId();   // ✅ direct, no parsing
        log.info("Fetching dashboard summary for userId: {}", userId);

        DashboardSummaryDTO dto = dashboardService.getDashboardSummary(userId);
        log.debug("✅ Dashboard summary generated");
        return ResponseEntity.ok(dto);
    }
}
