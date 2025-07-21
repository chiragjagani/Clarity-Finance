package com.cj.financeapp.services;

import com.cj.financeapp.dto.*;
import com.cj.financeapp.models.*;
import com.cj.financeapp.repositories.*;
import com.cj.financeapp.exceptions.*;
import com.cj.financeapp.mail.EmailService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowedServiceImpl implements BorrowedService {

    private final BorrowedRepository borrowedRepo;
    private final UserRepository userRepo;
    private final EmailService emailService;

    @Override
    public BorrowedResponseDTO createBorrowed(Long userId, BorrowedCreateDTO dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Borrowed borrowed = Borrowed.builder()
                .user(user)
                .lenderName(dto.lenderName)
                .lenderEmail(dto.lenderEmail)
                .amount(dto.amount)
                .borrowDate(dto.borrowDate)
                .reason(dto.reason)
                .paidBack(false)
                .build();
        borrowed = borrowedRepo.save(borrowed);
        return mapToResponse(borrowed);
    }

    @Override
    public List<BorrowedResponseDTO> listBorrowed(Long userId) {
        return borrowedRepo.findAllByUserId(userId)
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public BorrowedResponseDTO markAsPaidBack(Long id, Long userId, BorrowedMarkPaidDTO dto) {
        Borrowed borrowed = borrowedRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowed", "id", id));
        if (!borrowed.getUser().getId().equals(userId))
            throw new UnauthorizedException("You do not own this borrowed record");
        if (borrowed.isPaidBack())
            throw new BadRequestException("Already marked as paid");

        borrowed.setPaidBack(true);
        borrowed.setPaidBackDate(dto.paidBackDate != null ? dto.paidBackDate : LocalDate.now());
        borrowedRepo.save(borrowed);

        // Send email notification
        emailService.sendBorrowedRepaidEmail(borrowed.getLenderEmail(), borrowed.getUser().getName(), borrowed.getAmount());

        return mapToResponse(borrowed);
    }

    private BorrowedResponseDTO mapToResponse(Borrowed b) {
        BorrowedResponseDTO dto = new BorrowedResponseDTO();
        dto.id = b.getId();
        dto.lenderName = b.getLenderName();
        dto.lenderEmail = b.getLenderEmail();
        dto.amount = b.getAmount();
        dto.borrowDate = b.getBorrowDate();
        dto.reason = b.getReason();
        dto.paidBack = b.isPaidBack();
        dto.paidBackDate = b.getPaidBackDate();
        return dto;
    }
}