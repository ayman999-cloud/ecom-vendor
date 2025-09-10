package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
