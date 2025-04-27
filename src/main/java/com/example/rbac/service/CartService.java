package com.example.rbac.service;

import com.example.rbac.entity.CartItem;
import com.example.rbac.entity.User;
import com.example.rbac.mapper.CartItemMapper;
import com.example.rbac.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private UserMapper userMapper;

    public void addToCart(CartItem cartItem) {
        if (cartItem.getUserId() == null || cartItem.getProductId() == null) {
            throw new IllegalArgumentException("userId and productId must not be null");
        }
        Optional<CartItem> existingItem = cartItemMapper.findByUserIdAndProductId(
                cartItem.getUserId(), cartItem.getProductId());
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItem.getQuantity());
            cartItemMapper.update(item);
        } else {
            cartItemMapper.insert(cartItem);
        }
    }

    public List<CartItem> getCartItems(Long userId) {
        return cartItemMapper.findByUserId(userId);
    }

    public void updateCartItem(CartItem cartItem) {
        Optional<CartItem> existingItem = cartItemMapper.findByUserIdAndProductId(
                cartItem.getUserId(), cartItem.getProductId());
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(cartItem.getQuantity());
            cartItemMapper.update(item);
        } else {
            throw new RuntimeException("Cart item not found");
        }
    }

    public void removeFromCart(Long userId, Long productId) {
        cartItemMapper.deleteByUserIdAndProductId(userId, productId);
    }

    public void clearCart(Long userId) {
        cartItemMapper.deleteByUserId(userId);
    }

    public Long getUserIdByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user.getId();
    }
}