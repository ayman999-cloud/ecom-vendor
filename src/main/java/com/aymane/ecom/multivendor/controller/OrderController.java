package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.controller.response.PaymentLinkResponse;
import com.aymane.ecom.multivendor.domain.PaymentMethod;
import com.aymane.ecom.multivendor.model.*;
import com.aymane.ecom.multivendor.repository.PaymentOrderRepository;
import com.aymane.ecom.multivendor.service.*;
import com.razorpay.PaymentLink;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final PaymentService paymentService;
    private final PaymentOrderRepository paymentOrderRepository;

    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(@RequestHeader("Authorization") String jwt,
                                                                  @RequestBody Address shippingAddress,
                                                                  @RequestParam PaymentMethod paymentMethod) throws Exception {
        final User user = this.userService.findUserByJwtToken(jwt);

        final Cart cart = this.cartService.findUserCart(user);

        final Set<Order> orders = this.orderService.createOrder(user, shippingAddress, cart);

        final PaymentOrder paymentOrder = this.paymentService.createOrder(user, orders);

        final PaymentLinkResponse res = new PaymentLinkResponse();

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            final PaymentLink paymentLink = this.paymentService.createRazorPayPaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
            final String paymentUrl = paymentLink.get("short_url");
            final String paymentUrlIrd = paymentLink.get("id");

            res.setPaymentLinkUrl(paymentUrl);

            paymentOrder.setPaymentLinkId(paymentUrlIrd);

            this.paymentOrderRepository.save(paymentOrder);
        } else {
            final String stripePaymentLink = this.paymentService.createStripePaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
            res.setPaymentLinkUrl(stripePaymentLink);
        }

        return ResponseEntity.ok(res);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> userOrdersHistoryHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        final User user = this.userService.findUserByJwtToken(jwt);
        final List<Order> orders = this.orderService.userOrdersHistory(user.getId());
        return ResponseEntity.accepted().body(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId,
                                              @RequestHeader("Authorization") String jwt) throws Exception {
        final User user = this.userService.findUserByJwtToken(jwt);

        final Order order = this.orderService.findOrderById(orderId);

        if (!user.getId().equals(order.getUser().getId())) {
            throw new Exception("Unauthorized");
        }

        return ResponseEntity.accepted().body(order);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long orderItemId, @RequestHeader("Authorization") String jwt) throws Exception {
        final User user = this.userService.findUserByJwtToken(jwt);

        final OrderItem orderItem = this.orderService.findOrderItemById(orderItemId);

        if (!user.getId().equals(orderItem.getUserId())) {
            throw new Exception("Unauthorized");
        }

        return ResponseEntity.accepted().body(orderItem);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId,
                                             @RequestHeader("Authorization") String jwt) throws Exception {
        final User user = this.userService.findUserByJwtToken(jwt);
        final Order order = this.orderService.cancelOrder(orderId, user);

        final Seller seller = this.sellerService.getSellerById(order.getSellerId());
        final SellerReport sellerReport = this.sellerReportService.getSellerReport(seller);

        sellerReport.setCanceledOrders(sellerReport.getCanceledOrders() + 1);
        sellerReport.setTotalRefunds(sellerReport.getTotalRefunds() + order.getTotalSellingPrice());

        this.sellerReportService.updateSellerReport(sellerReport);

        return ResponseEntity.ok(order);
    }
}
