package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    List<Order> findBySellerId(Long sellerId);
}
