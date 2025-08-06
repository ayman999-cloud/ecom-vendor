package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
