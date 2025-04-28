package com.example.rbac.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status", nullable = false)
    private String status;

    @Embedded
    private ShippingAddress shippingAddress; // New field for address

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items;

    @Transient
    private List<OrderStatusHistory> statusHistory;

    // Constructors
    public Order() {
        this.status = "ORDER_PLACED";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public List<OrderStatusHistory> getStatusHistory() { return statusHistory; }
    public void setStatusHistory(List<OrderStatusHistory> statusHistory) { this.statusHistory = statusHistory; }
    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(ShippingAddress shippingAddress) { this.shippingAddress = shippingAddress; }
}

// New embedded class for address
@Embeddable
class ShippingAddress {
    @Column(name = "shipping_street")
    private String street;
    @Column(name = "shipping_city")
    private String city;
    @Column(name = "shipping_state")
    private String state;
    @Column(name = "shipping_postal_code")
    private String postalCode;
    @Column(name = "shipping_country")
    private String country;
    @Column(name = "shipping_name")
    private String name;
    @Column(name = "shipping_phone_number")
    private String phoneNumber;

    // Constructors
    public ShippingAddress() {}

    public ShippingAddress(String street, String city, String state, String postalCode, String country, String name, String phoneNumber) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}