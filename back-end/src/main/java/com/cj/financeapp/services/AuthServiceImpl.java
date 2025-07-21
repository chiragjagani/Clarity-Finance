package com.cj.financeapp.services;

import com.cj.financeapp.dto.JwtResponseDTO;
import com.cj.financeapp.dto.UserLoginDTO;
import com.cj.financeapp.dto.UserRegisterDTO;
import com.cj.financeapp.dto.UserResponseDTO;
import com.cj.financeapp.exceptions.BadRequestException;
import com.cj.financeapp.exceptions.UnauthorizedException;
import com.cj.financeapp.models.User;
import com.cj.financeapp.repositories.UserRepository;
import com.cj.financeapp.security.JwtUtil;
import com.cj.financeapp.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponseDTO register(UserRegisterDTO dto) {
        log.info("Attempting to register user with email: {}", dto.email);

        if (userRepository.existsByEmail(dto.email)) {
            log.warn("Registration failed: Email '{}' already exists", dto.email);
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .name(dto.name)
                .email(dto.email)
                .passwordHash(passwordEncoder.encode(dto.password))
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);
        log.info("User registered successfully with ID: {} and email: {}", saved.getId(), saved.getEmail());

        return new UserResponseDTO() {{
            id = saved.getId();
            name = saved.getName();
            email = saved.getEmail();
            role = saved.getRole();
        }};
    }

    @Override
    public JwtResponseDTO login(UserLoginDTO dto) {
        log.info("Entering login() - Login attempt for email: {}", dto.email);

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email, dto.password)
            );

            UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            log.info("Authentication successful for user: {}, ID: {}", userDetails.getEmail(), userDetails.getId());

            String jwt = jwtUtil.generateToken(userDetails.getId());
            log.info("JWT successfully generated for user ID: {}", userDetails.getId());

            JwtResponseDTO resp = new JwtResponseDTO();
            resp.token = jwt;
            resp.id = userDetails.getId();
            resp.email = userDetails.getEmail();
            resp.role = userDetails.getAuthorities().toString();

            log.info("Returning JWT response: [tokenType: Bearer, userId: {}, email: {}, role: {}]",
                    resp.id, resp.email, resp.role);

            return resp;

        } catch (BadCredentialsException ex) {
            log.warn("Login failed: Invalid credentials for email: {}", dto.email);
            throw new UnauthorizedException("Invalid credentials");
        } catch (AuthenticationException ex) {
            log.error("AuthenticationException occurred during login for email {}: {}", dto.email, ex.getMessage());
            throw new UnauthorizedException("Authentication failed");
        }
    }
}
