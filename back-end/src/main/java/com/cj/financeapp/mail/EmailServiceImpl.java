package com.cj.financeapp.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendBorrowedRepaidEmail(String to, String myName, BigDecimal amount) {
        log.info("📨 Preparing borrowed repaid email to: {}", to);
        String subject = "Your money has been returned";
        String message = String.format(
                "Hello,\n\nYour money has been returned by %s.\nAmount: ₹%s\n\nThank you.",
                myName, amount.stripTrailingZeros().toPlainString());

        log.debug("📄 Email subject: {}", subject);
        log.debug("✉️ Email body: {}", message);
        sendEmail(to, subject, message);
    }

    @Override
    public void sendLentReminderEmail(String to, String myName, BigDecimal amount, LocalDate dueDate) {
        log.info("📨 Preparing lent reminder email to: {}", to);
        String subject = "Repayment Reminder";
        String dateStr = dueDate != null ? " Please repay by: " + dueDate.toString() : "";
        String message = String.format(
                "Hello,\n\nYou borrowed ₹%s from %s.%s\nPlease repay as agreed.\n\nThank you.",
                amount.stripTrailingZeros().toPlainString(), myName, dateStr);

        log.debug("📄 Email subject: {}", subject);
        log.debug("✉️ Email body: {}", message);
        sendEmail(to, subject, message);
    }

    private void sendEmail(String to, String subject, String body) {
        log.info("📤 Sending email to: {}", to);

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);

            mailSender.send(msg);
            log.info("✅ Email sent successfully to: {}", to);

        } catch (Exception e) {
            log.error("❌ Failed to send email to: {} | Reason: {}", to, e.getMessage(), e);
        }
    }
}
