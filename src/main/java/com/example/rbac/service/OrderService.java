package com.example.rbac.service;

import com.example.rbac.entity.Order;
import com.example.rbac.entity.OrderItem;
import com.example.rbac.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final OrderMapper orderMapper;

    public OrderService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public void createOrder(Long userId, Double totalPrice, List<OrderItem> items) {
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("Pending"); // Set initial status
        order.setItems(items);

        // Insert the order
        orderMapper.insertOrder(order);

        // Associate items with the order and insert them
        for (OrderItem item : items) {
            item.setOrder(order);
            orderMapper.insertOrderItem(item);
        }
    }

    public List<Order> findOrdersByUserId(Long userId) {
        return orderMapper.findOrdersByUserId(userId);
    }

    public List<Order> findAllOrders() {
        return orderMapper.findAllOrders();
    }

    public void updateOrderStatus(Long orderId, String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("status", status);
        orderMapper.updateOrderStatus(params);
    }

    public Order findOrderById(Long orderId) {
        return orderMapper.findOrderById(orderId); // Add this method
    }
}