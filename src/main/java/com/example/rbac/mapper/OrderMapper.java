package com.example.rbac.mapper;

import com.example.rbac.entity.Order;
import com.example.rbac.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void insertOrder(Order order);

    void insertOrderItem(OrderItem orderItem);

    List<Order> findOrdersByUserId(Long userId);

    List<Order> findAllOrders();

    void updateOrderStatus(Map<String, Object> params);

    Order findOrderById(Long orderId); // Add this method
}