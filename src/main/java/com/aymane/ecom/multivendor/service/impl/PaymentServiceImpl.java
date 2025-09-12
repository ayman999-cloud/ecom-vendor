package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.domain.PaymentOrderStatus;
import com.aymane.ecom.multivendor.domain.PaymentStatus;
import com.aymane.ecom.multivendor.model.Order;
import com.aymane.ecom.multivendor.model.PaymentOrder;
import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.repository.OrderRepository;
import com.aymane.ecom.multivendor.repository.PaymentOrderRepository;
import com.aymane.ecom.multivendor.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.billingportal.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;

    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        final Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

        final PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);

        return this.paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long paymentOrderId) throws Exception {

        return paymentOrderRepository.findById(paymentOrderId).orElseThrow(() -> new Exception("Payment Order Not found"));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentLinkId) throws Exception {
        final PaymentOrder paymentOrder = this.paymentOrderRepository.findByPaymentLinkId(paymentLinkId);
        if (paymentOrder == null) throw new Exception("Payment order not found with paymentLinkId");
        return paymentOrder;
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder,
                                       String paymentId,
                                       String paymentLinkId) throws RazorpayException {
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            final RazorpayClient razorpayClient = new RazorpayClient("apiKey", "apiSecret");
            final Payment payment = razorpayClient.payments.fetch(paymentId);
            if (payment.get("status").equals("captured")) {
                final Set<Order> orders = paymentOrder.getOrders();
                for (Order order : orders) {
                    order.setPaymentStatus(PaymentStatus.COMPLETED);
                    orderRepository.save(order);
                }
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;
            }
            paymentOrder.setStatus(PaymentOrderStatus.FAILED);
            paymentOrderRepository.save(paymentOrder);
        }
        return false;
    }

    @Override
    public PaymentLink createRazorPayPaymentLink(User user, Long amount, Long paymentOrderId) throws RazorpayException {
        amount = amount * 100;

        try {
            final RazorpayClient razorpayClient = new RazorpayClient("apiKey", "apiSecret");
            final JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "EUR");

            final JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("callback_url", "http://localhost:3000/payment-success/" + paymentOrderId);
            paymentLinkRequest.put("callback_method", "get");

            final PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
            final String paymentLinkUrl = paymentLink.get("short_url");
            final String paymentLinkId = paymentLink.get("id");

            return paymentLink;

        } catch (Exception e) {
            throw new RazorpayException(e.getMessage());
        }
    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = "sk_test_51S6BPy35m2o6elTgCXg4JUTzRgaiF8smCg4EuMTOqJybq69uBSrpzeUxlOpMO9yq6oSnsRAAVPPdLIDj0dERwMNk00y61pv3ZB";

        final SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/" + orderId)
                .setCancelUrl("http://localhost:3000/payment-cancel/" + orderId)
                .addLineItem(SessionCreateParams.LineItem
                        .builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData
                                .builder()
                                .setCurrency("USD")
                                .setUnitAmount(amount * 100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                        .builder()
                                        .setName("Ecomm BZ payment")
                                        .build())
                                .build())
                        .build())
                .build();

        final Session session = Session.create(params.toMap());
        return session.getUrl();
    }
}
