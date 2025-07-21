package com.cj.financeapp.dto;

import jakarta.validation.constraints.NotBlank;

public class StockCreateDTO {
    @NotBlank
    public String symbol;
}
