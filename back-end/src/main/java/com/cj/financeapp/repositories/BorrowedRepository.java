package com.cj.financeapp.repositories;

import com.cj.financeapp.models.Borrowed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowedRepository extends JpaRepository<Borrowed, Long> {
    List<Borrowed> findAllByUserId(Long userId);
}