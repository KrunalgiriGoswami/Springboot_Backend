package com.example.rbac.mapper;

import com.example.rbac.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findByUsername(String username);
    void insertUser(User user);
    void insertUserRole(Long userId, Long roleId);
    Long findRoleIdByName(String roleName);
}