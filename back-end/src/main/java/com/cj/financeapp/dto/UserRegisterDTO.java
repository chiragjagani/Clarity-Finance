package com.cj.financeapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegisterDTO {
    @NotBlank
    @Size(min=2, max=50)
    public String name;
    @Email
    @NotBlank
    public String email;
    @NotBlank
    @Size(min=6, max=100)
    public String password;
}
