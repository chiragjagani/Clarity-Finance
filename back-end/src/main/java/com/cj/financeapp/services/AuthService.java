package com.cj.financeapp.services;

import com.cj.financeapp.dto.JwtResponseDTO;
import com.cj.financeapp.dto.UserLoginDTO;
import com.cj.financeapp.dto.UserRegisterDTO;
import com.cj.financeapp.dto.UserResponseDTO;

public interface AuthService {
    UserResponseDTO register(UserRegisterDTO dto);
    JwtResponseDTO login(UserLoginDTO dto);
}
