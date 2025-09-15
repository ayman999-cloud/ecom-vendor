package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findByCode(String code);
}
