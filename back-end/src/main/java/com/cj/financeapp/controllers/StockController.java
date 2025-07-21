package com.cj.financeapp.controllers;

import com.cj.financeapp.dto.*;
import com.cj.financeapp.security.UserPrincipal;
import com.cj.financeapp.services.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
@Slf4j
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping
    public ResponseEntity<StockResponseDTO> addStock(
            @Valid @RequestBody StockCreateDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        log.debug("📥 Add stock request: {}", dto);
        log.debug("🔍 Authenticated user: {}", userPrincipal.getUsername());

        Long userId = userPrincipal.getId();
        log.info("Adding stock for userId: {}", userId);

        StockResponseDTO resp = stockService.addStock(userId, dto);
        log.debug("✅ Stock added: {}", resp.getSymbol());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public ResponseEntity<List<StockResponseDTO>> listStocks(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        log.debug("📄 Listing stocks for user: {}", userPrincipal.getUsername());
        Long userId = userPrincipal.getId();

        List<StockResponseDTO> stocks = stockService.listStocks(userId);
        log.info("🔢 Total stocks found: {}", stocks.size());
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/live")
    public ResponseEntity<Map<String, Object>> getLiveStockInfo(
            @RequestParam List<String> symbols
    ) {
        log.debug("📡 Fetching live info for symbols: {}", symbols);
        Map<String, Object> liveInfo = stockService.fetchLiveStockInfo(symbols);
        log.debug("✅ Live stock info fetched");
        return ResponseEntity.ok(liveInfo);
    }
}
