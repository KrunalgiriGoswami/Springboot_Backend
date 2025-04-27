package com.example.rbac.controller;

import com.example.rbac.config.JwtUtil;
import com.example.rbac.dto.JwtResponse;
import com.example.rbac.dto.LoginRequest;
import com.example.rbac.dto.SignupRequest;
import com.example.rbac.entity.User;
import com.example.rbac.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        try {
            System.out.println("Signup attempt for username: " + signupRequest.getUsername() + ", role: " + signupRequest.getRole());
            User user = new User();
            user.setUsername(signupRequest.getUsername());
            user.setPassword(signupRequest.getPassword());
            user.setEmail(signupRequest.getEmail());
            userService.registerUser(user, signupRequest.getRole());
            System.out.println("Signup successful for username: " + signupRequest.getUsername());
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            System.out.println("Signup failed for username: " + signupRequest.getUsername() + ", error: " + e.getClass().getName() + ": " + e.getMessage());
            return ResponseEntity.status(400).body("Signup failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Attempting login for username: " + loginRequest.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println("User authenticated: " + userDetails.getUsername() + ", Authorities: " + userDetails.getAuthorities());
            String jwt = jwtUtil.generateToken(userDetails);
            User user = userService.findByUsername(loginRequest.getUsername());
            String role = user.getRoles().get(0).getName().replace("ROLE_", "");
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), role));
        } catch (BadCredentialsException e) {
            System.out.println("Bad credentials for username: " + loginRequest.getUsername());
            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed for username: " + loginRequest.getUsername() + ", Reason: " + e.getMessage());
            return ResponseEntity.status(403).body("Authentication failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error during login: " + e.getMessage());
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }
}