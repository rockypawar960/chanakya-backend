package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.UserDashboardDTO;
import com.chanakya.repository.UserRepository;
import com.chanakya.service.UserDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserDashboardController {

    private final UserRepository userRepository;
    private final UserDashboardService userDashboardService;
    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/dashboard")
    public ApiResponse<UserDashboardDTO> getDashboard() {

        long userId=extractUserIdFromAuthentication();

        UserDashboardDTO dashboard = userDashboardService.getDashboard(userId);

        return new ApiResponse<>(true, "Dashboard fetched successfully", dashboard);
    }

    private Long extractUserIdFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("User not authenticated");
        }

        String userEmail = authentication.getName();

        return userRepository.findByEmail(userEmail)
                .map(com.chanakya.entity.User::getId)
                .orElseThrow(() -> new RuntimeException("User not found in DB: " + userEmail));
    }
}