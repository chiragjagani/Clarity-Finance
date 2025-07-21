package com.cj.financeapp.services;

import com.cj.financeapp.dto.AiTipsDTO;

public interface AiTipsService {
    AiTipsDTO generateTips(Long userId);
}