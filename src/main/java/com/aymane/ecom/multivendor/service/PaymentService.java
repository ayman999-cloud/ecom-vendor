package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.model.Order;
import com.aymane.ecom.multivendor.model.PaymentOrder;
import com.aymane.ecom.multivendor.model.User;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createOrder(User user, Set<Order> orders);

    PaymentOrder getPaymentOrderById(Long orderId) throws Exception;

    PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception;

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder,
                                String paymentId,
                                String paymentLinkId) throws RazorpayException;

    PaymentLink createRazorPayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException;

    String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
