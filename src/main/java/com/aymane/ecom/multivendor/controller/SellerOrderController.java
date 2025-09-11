package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.domain.OrderStatus;
import com.aymane.ecom.multivendor.exception.SellerException;
import com.aymane.ecom.multivendor.model.Order;
import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.service.OrderService;
import com.aymane.ecom.multivendor.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/orders")
public class SellerOrderController {
    private final OrderService orderService;
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrdersHandler(@RequestHeader("Authorization") String jwt) throws SellerException {
        final Seller seller = this.sellerService.getSellerProfile(jwt);
        final List<Order> orders = this.orderService.sellerOrders(seller.getId());
        return ResponseEntity.accepted().body(orders);
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderHandler(@RequestHeader("Authorization") String jwt,
                                                    @PathVariable Long orderId,
                                                    @PathVariable OrderStatus orderStatus) throws Exception {
        final Seller seller = this.sellerService.getSellerProfile(jwt);
        final Order order = this.orderService.updateOrderStatus(orderId, orderStatus);
        if (!seller.getId().equals(order.getSellerId())) {
            throw new Exception("Unauthorized to update the order");
        }
        return ResponseEntity.accepted().body(order);
    }
}
