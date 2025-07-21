package com.cj.financeapp.dto;

public class JwtResponseDTO {
    public String token;
    public String tokenType = "Bearer";
    public Long id;
    public String email;
    public String role;
}
