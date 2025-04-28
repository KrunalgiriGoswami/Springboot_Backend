package com.example.rbac.controller;

import com.example.rbac.entity.Order;
import com.example.rbac.entity.OrderItem;
import com.example.rbac.entity.OrderStatusHistory;
import com.example.rbac.entity.ShippingAddress;
import com.example.rbac.service.OrderService;
import com.example.rbac.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody Map<String, Object> orderData, Authentication authentication) {
        try {
            System.out.println("Creating order, Authentication: " + authentication);
            String username = authentication.getName();
            Long userId = getUserIdByUsername(username);
            Double totalPrice = Double.valueOf(orderData.get("totalPrice").toString());
            List<Map<String, Object>> itemsData = (List<Map<String, Object>>) orderData.get("items");
            List<OrderItem> items = itemsData.stream().map(itemData -> {
                OrderItem item = new OrderItem();
                item.setProductId(Long.valueOf(itemData.get("productId").toString()));
                item.setQuantity(Integer.valueOf(itemData.get("quantity").toString()));
                item.setPrice(Double.valueOf(itemData.get("price").toString()));
                item.setImageUrl(itemData.get("imageUrl") != null ? itemData.get("imageUrl").toString() : null);
                return item;
            }).collect(Collectors.toList());

            // Extract and handle shipping address with null checks
            Map<String, String> shippingData = (Map<String, String>) orderData.get("shippingAddress");
            ShippingAddress shippingAddress = new ShippingAddress(
                    shippingData != null ? shippingData.getOrDefault("street", "N/A") : "N/A",
                    shippingData != null ? shippingData.getOrDefault("city", "N/A") : "N/A",
                    shippingData != null ? shippingData.getOrDefault("state", "N/A") : "N/A",
                    shippingData != null ? shippingData.getOrDefault("postalCode", "N/A") : "N/A",
                    shippingData != null ? shippingData.getOrDefault("country", "N/A") : "N/A"
            );

            orderService.createOrder(userId, totalPrice, items, shippingAddress);
            return ResponseEntity.ok("Order created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create order: " + e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getUserOrders(Authentication authentication) {
        try {
            System.out.println("Fetching user orders, Authentication: " + authentication);
            if (authentication == null) {
                System.out.println("Authentication is null");
                return ResponseEntity.status(403).body(null);
            }
            String username = authentication.getName();
            System.out.println("Username from authentication: " + username);
            Long userId = getUserIdByUsername(username);
            System.out.println("User Id: " + userId);
            List<Order> orders = orderService.findOrdersByUserId(userId);
            orders.forEach(order -> order.setStatusHistory(orderService.findStatusHistoryByOrderId(order.getId())));
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            System.out.println("Error fetching orders: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders(Authentication authentication) {
        try {
            System.out.println("Fetching all orders, Authentication: " + authentication);
            if (authentication == null || !authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                System.out.println("User is not an admin");
                return ResponseEntity.status(403).body(null);
            }
            List<Order> orders = orderService.findAllOrders();
            orders.forEach(order -> order.setStatusHistory(orderService.findStatusHistoryByOrderId(order.getId())));
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            System.out.println("Error fetching all orders: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(403).body("Access Denied: Authentication required");
            }
            String username = authentication.getName();
            Long userId = getUserIdByUsername(username);
            Order order = orderService.findOrderById(orderId);
            if (order == null) {
                return ResponseEntity.status(404).body("Order not found");
            }
            String newStatus = requestBody.get("status");
            if (newStatus == null || !Arrays.asList("ORDER_PLACED", "PROCESSING", "OUT_FOR_DELIVERY", "SHIPPED", "DELIVERED").contains(newStatus)) {
                return ResponseEntity.status(400).body("Invalid status");
            }
            orderService.updateOrderStatus(orderId, newStatus);
            return ResponseEntity.ok("Order status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update order status: " + e.getMessage());
        }
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long orderId,
            Authentication authentication) {
        try {
            System.out.println("Cancelling order, Authentication: " + authentication);
            if (authentication == null) {
                System.out.println("Authentication is null");
                return ResponseEntity.status(403).body("Access Denied: Authentication required");
            }
            String username = authentication.getName();
            Long userId = getUserIdByUsername(username);
            Order order = orderService.findOrderById(orderId);
            if (order == null) {
                return ResponseEntity.status(404).body("Order not found");
            }
            if (!order.getUserId().equals(userId)) {
                return ResponseEntity.status(403).body("Access Denied: Not your order");
            }
            orderService.updateOrderStatus(orderId, "CANCELLED");
            return ResponseEntity.ok("Order cancelled successfully");
        } catch (Exception e) {
            System.out.println("Error cancelling order: " + e.getMessage());
            return ResponseEntity.status(500).body("Failed to cancel order: " + e.getMessage());
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(
            @PathVariable Long orderId,
            Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(403).body("Access Denied: Authentication required");
            }
            String username = authentication.getName();
            Long userId = getUserIdByUsername(username);
            Order order = orderService.findOrderById(orderId);
            if (order == null) {
                return ResponseEntity.status(404).body("Order not found");
            }
            if (!order.getUserId().equals(userId)) {
                return ResponseEntity.status(403).body("Access Denied: Not your order");
            }
            // Allow deletion for both DELIVERED and CANCELLED orders
            if (!order.getStatus().equals("DELIVERED") && !order.getStatus().equals("CANCELLED")) {
                return ResponseEntity.status(400).body("Only delivered or cancelled orders can be deleted");
            }
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok("Order deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete order: " + e.getMessage());
        }
    }

    private Long getUserIdByUsername(String username) {
        var user = userService.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        return user.getId();
    }
}