package com.chanakya.service;

import com.chanakya.dto.LoginRequest;
import com.chanakya.dto.LoginResponse;
import com.chanakya.dto.RegisterRequest;
import com.chanakya.entity.PasswordResetToken;
import com.chanakya.entity.Role;
import com.chanakya.entity.User;
import com.chanakya.exception.BadRequestException;
import com.chanakya.exception.ResourceNotFoundException;
import com.chanakya.repository.PasswordResetTokenRepository;
import com.chanakya.repository.RoleRepository;
import com.chanakya.repository.UserRepository;
import com.chanakya.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordResetTokenRepository tokenRepository;

    /**
     * Register a new user
     */
    public User register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        // Create new user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .isActive(true)
                .build();

        // Get or create USER role
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    newRole.setDescription("Standard user role");
                    return roleRepository.save(newRole);
                });

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());

        return savedUser;
    }

    /**
     * Authenticate user and generate tokens
     */
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get user details
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        // Get user role
        String role = user.getRoles().stream()
                .findFirst()
                .map(r -> r.getName().replace("ROLE_", ""))
                .orElse("USER");

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .role(role)
                .build();
    }

    /**
     * Refresh access token using refresh token
     */
    public LoginResponse refreshToken(String refreshToken) {
        log.info("Refreshing token");

        // Validate refresh token
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BadRequestException("Invalid or expired refresh token");
        }

        // Get email from token
        String email = jwtTokenProvider.getEmailFromToken(refreshToken);

        // Get user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Generate new tokens
        String newAccessToken = jwtTokenProvider.generateAccessTokenFromEmail(email, "ROLE_USER");
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);

        // Get user role
        String role = user.getRoles().stream()
                .findFirst()
                .map(r -> r.getName().replace("ROLE_", ""))
                .orElse("USER");

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .role(role)
                .build();
    }

    /**
     * Logout user
     */
    public void logout() {
        log.info("Logging out user");
        SecurityContextHolder.clearContext();
        // Additional logout logic (token blacklisting) can be added here
    }

    // 🔹 1. Forgot Password
    public String forgotPassword(String email) {

        Optional<User> userOpt = userRepository.findByEmail(email);

        // Security (important)
        if (userOpt.isEmpty()) {
            return "If email exists, link sent!";
        }

        // Old tokens delete
        tokenRepository.deleteByEmail(email);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiryTime(LocalDateTime.now().plusMinutes(15));

        tokenRepository.save(resetToken);

        String link = "http://localhost:5173/reset-password?token=" + token;

        // TODO: Email send karo
        System.out.println("RESET LINK: " + link);

        return "Reset link sent!";
    }

    // 🔹 2. Reset Password
    public String resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // delete token
        tokenRepository.deleteByEmail(user.getEmail());

        return "Password updated successfully!";
    }
}