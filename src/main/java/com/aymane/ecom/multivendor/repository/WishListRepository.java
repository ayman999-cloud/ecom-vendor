package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    WishList findByUserId(Long userId);
}
