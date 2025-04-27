package com.example.rbac.service;

import com.example.rbac.entity.User;
import com.example.rbac.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user, String roleName) {
        try {
            System.out.println("Registering user: " + user.getUsername() + ", role: " + roleName);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println("Inserting user into database: " + user.getUsername());
            userMapper.insertUser(user);
            if (user.getId() == null) {
                throw new RuntimeException("Failed to retrieve user ID after insertion");
            }
            String role = "ROLE_" + roleName.toUpperCase();
            System.out.println("Looking up role: " + role);
            Long roleId = userMapper.findRoleIdByName(role);
            if (roleId == null) {
                System.out.println("Role not found: " + role);
                throw new IllegalArgumentException("Invalid role: " + roleName);
            }
            System.out.println("Assigning role ID: " + roleId + " to user ID: " + user.getId());
            userMapper.insertUserRole(user.getId(), roleId);
            System.out.println("User registered successfully: " + user.getUsername());
        } catch (Exception e) {
            System.out.println("Error registering user: " + user.getUsername() + ", details: " + e.getClass().getName() + ": " + e.getMessage());
            throw e;
        }
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }


}