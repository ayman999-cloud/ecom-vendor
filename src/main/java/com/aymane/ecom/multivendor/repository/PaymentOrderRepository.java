package com.aymane.ecom.multivendor.repository;


import com.aymane.ecom.multivendor.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    PaymentOrder findByPaymentLinkId(String paymentLinkId);
}
