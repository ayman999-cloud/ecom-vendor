package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    public Seller findByEmail(String email);
}
