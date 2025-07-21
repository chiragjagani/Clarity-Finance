package com.cj.financeapp.repositories;

import com.cj.financeapp.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findAllByUserId(Long userId);
}