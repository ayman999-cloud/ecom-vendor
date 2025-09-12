package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySellerId(Long sellerId);
}
