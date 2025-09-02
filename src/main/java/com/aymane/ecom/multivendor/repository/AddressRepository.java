package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
