package com.cj.financeapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserLoginDTO {
    @Email
    @NotBlank
    public String email;
    @NotBlank
    public String password;
}
