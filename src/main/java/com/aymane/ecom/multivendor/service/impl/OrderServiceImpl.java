package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.domain.OrderStatus;
import com.aymane.ecom.multivendor.domain.PaymentStatus;
import com.aymane.ecom.multivendor.model.*;
import com.aymane.ecom.multivendor.repository.AddressRepository;
import com.aymane.ecom.multivendor.repository.OrderItemRepository;
import com.aymane.ecom.multivendor.repository.OrderRepository;
import com.aymane.ecom.multivendor.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if (!user.getAddresses().contains(shippingAddress)) {
            user.getAddresses().add(shippingAddress);
            addressRepository.save(shippingAddress);
        }

        final Map<Long, List<CartItem>> cartItemsBySeller =
                cart.getCartItems().stream().collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));

        final Set<Order> orders = new HashSet<>();

        for (Map.Entry<Long, List<CartItem>> entry : cartItemsBySeller.entrySet()) {
            final Long sellerId = entry.getKey();
            final List<CartItem> sellerCartItems = entry.getValue();

            final int totalMrpPrice = sellerCartItems.stream().mapToInt(CartItem::getMrpPrice).sum();

            final int totalOrderPrice = sellerCartItems.stream().mapToInt(CartItem::getSellingPrice).sum();

            final int totalItem = sellerCartItems.stream().mapToInt(CartItem::getQuantity).sum();

            final Order order = new Order();
            order.setUser(user);
            order.setSellerId(sellerId);
            order.setTotalMrpPrice(totalMrpPrice);
            order.setTotalSellingPrice(totalOrderPrice);
            order.setTotalItem(totalItem);
            order.setShippingAddress(shippingAddress);
            order.setOrderStatus(OrderStatus.PENDING);
            order.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            final Order savedOrder = orderRepository.save(order);
            orders.add(savedOrder);

            // Create order items
            final List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem item : sellerCartItems) {
                final OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setSellingPrice(item.getSellingPrice());
                savedOrder.getOrderItems().add(orderItem);

                final OrderItem savedOderItem = orderItemRepository.save(orderItem);
                orderItems.add(savedOderItem);
            }
        }

        return orders;
    }

    @Override
    public Order findOrderById(Long id) throws Exception {
        return this.orderRepository.findById(id).orElseThrow(() -> new Exception("Order not found with id " + id));
    }

    @Override
    public List<Order> userOrdersHistory(Long userId) {
        return this.orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellerOrders(Long sellerId) {
        return this.orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        final Order order = this.findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return this.orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        final Order order = findOrderById(orderId);
        if (!user.getId().equals(order.getUser().getId())) {
            throw new Exception("Unauthorized");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return this.orderRepository.save(order);
    }

    @Override
    public OrderItem findOrderItemById(Long id) throws Exception {
        return this.orderItemRepository.findById(id).orElseThrow(() -> new Exception("Order Item not found"));
    }
}
