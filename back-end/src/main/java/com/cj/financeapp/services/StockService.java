package com.cj.financeapp.services;

import com.cj.financeapp.dto.StockCreateDTO;
import com.cj.financeapp.dto.StockResponseDTO;

import java.util.List;
import java.util.Map;

public interface StockService {
    StockResponseDTO addStock(Long userId, StockCreateDTO dto);
    List<StockResponseDTO> listStocks(Long userId);
    Map<String, Object> fetchLiveStockInfo(List<String> symbols);
}