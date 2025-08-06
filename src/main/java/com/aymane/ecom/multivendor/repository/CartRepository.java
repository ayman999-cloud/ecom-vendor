package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
