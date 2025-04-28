package com.example.rbac.service;

import com.example.rbac.entity.Order;
import com.example.rbac.entity.OrderItem;
import com.example.rbac.entity.OrderStatusHistory;
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
        order.setStatus("ORDER_PLACED");
        orderMapper.insertOrder(order);

        for (OrderItem item : items) {
            item.setOrder(order);
            orderMapper.insertOrderItem(item);
        }

        orderMapper.insertOrderStatusHistory(new OrderStatusHistory(order.getId(), "ORDER_PLACED"));
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

        orderMapper.insertOrderStatusHistory(new OrderStatusHistory(orderId, status));
    }

    public Order findOrderById(Long orderId) {
        return orderMapper.findOrderById(orderId);
    }

    // New method to fetch status history
    public List<OrderStatusHistory> findStatusHistoryByOrderId(Long orderId) {
        return orderMapper.findStatusHistoryByOrderId(orderId);
    }
}