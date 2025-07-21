package com.cj.financeapp.services;

import com.cj.financeapp.dto.*;
import com.cj.financeapp.exceptions.*;
import com.cj.financeapp.mail.EmailService;
import com.cj.financeapp.models.*;
import com.cj.financeapp.repositories.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LentServiceImpl implements LentService {

    private final LentRepository lentRepo;
    private final UserRepository userRepo;
    private final EmailService emailService;

    @Override
    public LentResponseDTO createLent(Long userId, LentCreateDTO dto) {
        log.debug("📥 Creating Lent entry for userId: {}, data: {}", userId, dto);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.error("❌ User not found for ID: {}", userId);
                    return new ResourceNotFoundException("User", "id", userId);
                });

        Lent lent = Lent.builder()
                .user(user)
                .borrowerName(dto.borrowerName)
                .borrowerEmail(dto.borrowerEmail)
                .amount(dto.amount)
                .lendDate(dto.lendDate)
                .reason(dto.reason)
                .dueDate(dto.dueDate)
                .repaid(false)
                .build();

        lent = lentRepo.save(lent);
        log.info("✅ Lent entry created with ID: {}", lent.getId());

        // 📧 Send immediate email notification (optional)
        try {
            emailService.sendLentReminderEmail(
                    lent.getBorrowerEmail(),
                    user.getName(),
                    lent.getAmount(),
                    lent.getDueDate()
            );
            log.info("📧 Initial reminder email sent to {}", lent.getBorrowerEmail());
        } catch (Exception ex) {
            log.warn("⚠️ Failed to send initial reminder email to {}: {}", lent.getBorrowerEmail(), ex.getMessage());
        }

        return mapToResponse(lent);
    }

    @Override
    public List<LentResponseDTO> listLent(Long userId) {
        log.debug("📄 Listing lent records for userId: {}", userId);
        return lentRepo.findAllByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void sendReminder(Long id, Long userId) {
        log.debug("📨 Sending repayment reminder for Lent ID: {} and userId: {}", id, userId);

        Lent lent = lentRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("❌ Lent record not found for ID: {}", id);
                    return new ResourceNotFoundException("Lent", "id", id);
                });

        if (!lent.getUser().getId().equals(userId)) {
            log.warn("🚫 Unauthorized attempt to send reminder for Lent ID: {}", id);
            throw new UnauthorizedException("You do not own this lent record");
        }

        emailService.sendLentReminderEmail(
                lent.getBorrowerEmail(),
                lent.getUser().getName(),
                lent.getAmount(),
                lent.getDueDate()
        );
        log.info("📧 Reminder email sent to: {}", lent.getBorrowerEmail());
    }

    @Override
    public LentResponseDTO markAsRepaid(Long id, Long userId, LentMarkPaidDTO dto) {
        log.debug("💰 Marking Lent ID {} as repaid by userId: {}", id, userId);

        Lent lent = lentRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("❌ Lent record not found for ID: {}", id);
                    return new ResourceNotFoundException("Lent", "id", id);
                });

        if (!lent.getUser().getId().equals(userId)) {
            log.warn("🚫 Unauthorized attempt to mark repaid for Lent ID: {}", id);
            throw new UnauthorizedException("You do not own this lent record");
        }

        if (lent.isRepaid()) {
            log.warn("⚠️ Lent ID {} is already marked as repaid", id);
            throw new BadRequestException("Already marked as repaid");
        }

        lent.setRepaid(true);
        lent.setRepaidDate(dto.repaidDate != null ? dto.repaidDate : LocalDate.now());
        lentRepo.save(lent);

        log.info("✅ Lent ID {} marked as repaid", id);
        return mapToResponse(lent);
    }

    private LentResponseDTO mapToResponse(Lent l) {
        LentResponseDTO dto = new LentResponseDTO();
        dto.id = l.getId();
        dto.borrowerName = l.getBorrowerName();
        dto.borrowerEmail = l.getBorrowerEmail();
        dto.amount = l.getAmount();
        dto.lendDate = l.getLendDate();
        dto.reason = l.getReason();
        dto.dueDate = l.getDueDate();
        dto.repaid = l.isRepaid();
        dto.repaidDate = l.getRepaidDate();
        return dto;
    }
}
