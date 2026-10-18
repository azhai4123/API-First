package com.shop.service;

import com.shop.entity.Customer;
import com.shop.exception.ApiException;
import com.shop.exception.ConflictException;
import com.shop.exception.ResourceNotFoundException;
import com.shop.model.AuthResponse;
import com.shop.model.AuthUser;
import com.shop.model.LoginRequest;
import com.shop.model.RegisterRequest;
import com.shop.repository.CustomerRepository;
import com.shop.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        // Fail-Fast FF-05: Kiểm tra email trùng
        if (customerRepository.existsByEmail(req.getEmail())) {
            throw new ConflictException("EMAIL_ALREADY_EXISTS", "Email '" + req.getEmail() + "' đã được sử dụng");
        }

        Customer customer = Customer.builder()
                .email(req.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName().trim())
                .phone(req.getPhone().trim())
                .address(req.getAddress().trim())
                .avatarUrl(req.getAvatarUrl() != null ? req.getAvatarUrl().toString() : "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150")
                .role("CUSTOMER")
                .status("ACTIVE")
                .build();

        Customer saved = customerRepository.save(customer);

        String token = jwtTokenProvider.generateToken(saved.getId(), saved.getEmail(), saved.getRole());

        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setUser(toAuthUser(saved));
        return resp;
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        Customer customer = customerRepository.findByEmail(req.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Email hoặc mật khẩu không chính xác"));

        if (!passwordEncoder.matches(req.getPassword(), customer.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Email hoặc mật khẩu không chính xác");
        }

        String token = jwtTokenProvider.generateToken(customer.getId(), customer.getEmail(), customer.getRole());

        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setUser(toAuthUser(customer));
        return resp;
    }

    @Transactional(readOnly = true)
    public AuthUser getCurrentUser(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "Không tìm thấy hồ sơ người dùng"));
        return toAuthUser(customer);
    }

    private AuthUser toAuthUser(Customer c) {
        AuthUser user = new AuthUser();
        user.setId(c.getId());
        user.setEmail(c.getEmail());
        user.setFullName(c.getFullName());
        if (c.getRole() != null) {
            try {
                user.setRole(AuthUser.RoleEnum.fromValue(c.getRole()));
            } catch (Exception e) {
                user.setRole(AuthUser.RoleEnum.CUSTOMER);
            }
        }
        user.setAvatarUrl(c.getAvatarUrl());
        return user;
    }
}
