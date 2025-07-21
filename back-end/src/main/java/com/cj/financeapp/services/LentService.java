package com.cj.financeapp.services;

import com.cj.financeapp.dto.*;
import java.util.List;

public interface LentService {
    LentResponseDTO createLent(Long userId, LentCreateDTO dto);
    List<LentResponseDTO> listLent(Long userId);
    void sendReminder(Long id, Long userId);
    LentResponseDTO markAsRepaid(Long id, Long userId, LentMarkPaidDTO dto);
}