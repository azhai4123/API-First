package com.shop.controller;

import com.shop.api.AuthApi;
import com.shop.model.AuthResponse;
import com.shop.model.AuthUser;
import com.shop.model.LoginRequest;
import com.shop.model.RegisterRequest;
import com.shop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<AuthUser> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth != null ? auth.getName() : "admin@shop.com";
        return ResponseEntity.ok(authService.getCurrentUser(email));
    }

    @Override
    public ResponseEntity<AuthResponse> loginUser(LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Override
    public ResponseEntity<AuthResponse> registerCustomer(RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
