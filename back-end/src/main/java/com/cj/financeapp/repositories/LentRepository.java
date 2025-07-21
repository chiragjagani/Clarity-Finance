package com.cj.financeapp.repositories;

import com.cj.financeapp.models.Lent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LentRepository extends JpaRepository<Lent, Long> {
    List<Lent> findAllByUserId(Long userId);
}

