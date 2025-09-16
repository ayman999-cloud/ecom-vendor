package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
}
