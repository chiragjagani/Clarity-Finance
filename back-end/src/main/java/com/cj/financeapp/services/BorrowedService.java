package com.cj.financeapp.services;

import com.cj.financeapp.dto.*;

import java.util.List;

public interface BorrowedService {
    BorrowedResponseDTO createBorrowed(Long userId, BorrowedCreateDTO dto);

    List<BorrowedResponseDTO> listBorrowed(Long userId);

    BorrowedResponseDTO markAsPaidBack(Long id, Long userId, BorrowedMarkPaidDTO dto);
}