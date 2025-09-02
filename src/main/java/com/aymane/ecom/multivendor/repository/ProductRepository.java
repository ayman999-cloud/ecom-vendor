package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
