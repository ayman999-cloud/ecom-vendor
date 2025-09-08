package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
