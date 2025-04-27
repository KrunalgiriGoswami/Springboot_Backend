package com.example.rbac.mapper;

import com.example.rbac.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Optional;

@Mapper
public interface CartItemMapper {
    void insert(CartItem cartItem);
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
    List<CartItem> findByUserId(Long userId);
    void update(CartItem cartItem);
    void deleteByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserId(Long userId);
}