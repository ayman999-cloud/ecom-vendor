package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.domain.AccountStatus;
import com.aymane.ecom.multivendor.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus accountStatus);
}
