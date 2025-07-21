package com.cj.financeapp.controllers;

import com.cj.financeapp.dto.JwtResponseDTO;
import com.cj.financeapp.dto.UserLoginDTO;
import com.cj.financeapp.dto.UserRegisterDTO;
import com.cj.financeapp.dto.UserResponseDTO;
import com.cj.financeapp.services.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Validated
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // User Registration
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegisterDTO dto) {
        UserResponseDTO savedUser = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // Login & JWT Issuance
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody UserLoginDTO dto) {
        log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        log.info("Login attempt for user: {}", dto.email);
        JwtResponseDTO resp = authService.login(dto);
        log.info("JWT issued for user: {}", resp.email);
        return ResponseEntity.ok(resp);
    }
}
