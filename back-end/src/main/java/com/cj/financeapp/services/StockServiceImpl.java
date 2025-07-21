package com.cj.financeapp.services;

import com.cj.financeapp.dto.StockCreateDTO;
import com.cj.financeapp.dto.StockResponseDTO;
import com.cj.financeapp.exceptions.ResourceNotFoundException;
import com.cj.financeapp.models.Stock;
import com.cj.financeapp.models.User;
import com.cj.financeapp.repositories.StockRepository;
import com.cj.financeapp.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepo;
    private final UserRepository userRepo;

    @Value("${stock.api.key}")
    private String stockApiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://www.alphavantage.co")
            .build();

    @Override
    public StockResponseDTO addStock(Long userId, StockCreateDTO dto) {
        log.debug("📥 Add stock request: {}", dto);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Stock stock = Stock.builder()
                .user(user)
                .symbol(dto.symbol.toUpperCase())
                .addedAt(LocalDateTime.now())
                .build();

        stockRepo.save(stock);
        log.info("✅ Stock {} added for userId {}", stock.getSymbol(), userId);

        StockResponseDTO resp = new StockResponseDTO();
        resp.id = stock.getId();
        resp.symbol = stock.getSymbol();
        return resp;
    }

    @Override
    public List<StockResponseDTO> listStocks(Long userId) {
        log.debug("📄 Listing saved stocks for userId {}", userId);

        return stockRepo.findAllByUserId(userId)
                .stream()
                .map(stock -> {
                    StockResponseDTO dto = new StockResponseDTO();
                    dto.id = stock.getId();
                    dto.symbol = stock.getSymbol();
                    return dto;
                })
                .toList();
    }

    @Override
    public Map<String, Object> fetchLiveStockInfo(List<String> symbols) {
        Map<String, Object> result = new LinkedHashMap<>();

        for (String rawSymbol : symbols) {
            String symbol = rawSymbol.trim().toUpperCase();
            log.debug("📡 Fetching TIME_SERIES_DAILY for {}", symbol);

            String uri = String.format(
                    "/query?function=TIME_SERIES_DAILY&symbol=%s&outputsize=compact&apikey=%s",
                    symbol, stockApiKey
            );

            try {
                Map<?, ?> response = webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();

                Map<String, Object> timeSeries = (Map<String, Object>) response.get("Time Series (Daily)");

                if (timeSeries != null && !timeSeries.isEmpty()) {
                    List<Map<String, Object>> dataList = new ArrayList<>();

                    timeSeries.entrySet().stream()
                            .sorted(Map.Entry.<String, Object>comparingByKey().reversed()) // latest first
                            .limit(30) // return last 30 days
                            .forEach(entry -> {
                                String date = entry.getKey();
                                Map<String, String> values = (Map<String, String>) entry.getValue();

                                Map<String, Object> day = new LinkedHashMap<>();
                                day.put("date", date);
                                day.put("open", new BigDecimal(values.get("1. open")));
                                day.put("high", new BigDecimal(values.get("2. high")));
                                day.put("low", new BigDecimal(values.get("3. low")));
                                day.put("close", new BigDecimal(values.get("4. close")));
                                day.put("volume", Long.parseLong(values.get("5. volume")));
                                dataList.add(day);
                            });

                    result.put(symbol, dataList);
                    log.info("✅ {} time series data fetched ({} days)", symbol, dataList.size());

                } else {
                    log.warn("⚠️ Time series data missing for {}", symbol);
                    result.put(symbol, Map.of("error", "Time series data missing"));
                }

            } catch (Exception e) {
                log.error("❌ Failed to fetch data for {}: {}", symbol, e.getMessage());
                result.put(symbol, Map.of("error", "Fetch failed"));
            }
        }

        return result;
    }


}
