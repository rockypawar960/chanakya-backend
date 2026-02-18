package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.LoginRequest;
import com.chanakya.dto.LoginResponse;
import com.chanakya.dto.RegisterRequest;
import com.chanakya.entity.User;
import com.chanakya.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login endpoint called for email: {}", request.getEmail());

        LoginResponse loginResponse = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(loginResponse)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Create a new user account")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register endpoint called for email: {}", request.getEmail());

        User user = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<User>builder()
                        .success(true)
                        .message("Registration successful")
                        .data(user)
                        .status(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Logout current user")
    public ResponseEntity<ApiResponse<Void>> logout() {
        log.info("Logout endpoint called");

        authService.logout();

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Logout successful")
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh token", description = "Get new access token using refresh token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestParam String refreshToken) {
        log.info("Refresh token endpoint called");

        LoginResponse loginResponse = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .success(true)
                        .message("Token refreshed successfully")
                        .data(loginResponse)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}