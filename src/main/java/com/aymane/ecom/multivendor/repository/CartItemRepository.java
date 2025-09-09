package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.Cart;
import com.aymane.ecom.multivendor.model.CartItem;
import com.aymane.ecom.multivendor.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
