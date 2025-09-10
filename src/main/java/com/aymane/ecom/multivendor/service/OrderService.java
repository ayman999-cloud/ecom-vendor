package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.domain.OrderStatus;
import com.aymane.ecom.multivendor.model.*;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);

    Order findOrderById(Long id) throws Exception;

    List<Order> userOrdersHistory(Long userId);

    List<Order> sellerOrders(Long sellerId);

    Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;

    Order cancelOrder(Long orderId, User user) throws Exception;

    OrderItem findOrderItemById(Long id) throws Exception;
}
