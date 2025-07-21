package com.cj.financeapp.services;

import com.cj.financeapp.dto.DashboardSummaryDTO;

public interface DashboardService {
    DashboardSummaryDTO getDashboardSummary(Long userId);
}