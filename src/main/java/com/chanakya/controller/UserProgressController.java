package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.ProgressRequest;
import com.chanakya.repository.UserRepository;
import com.chanakya.service.UserProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/progress")
@RequiredArgsConstructor
public class UserProgressController {

    private final UserProgressService progressService;
    private final UserRepository userRepository;

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<String>> updateProgress(@RequestBody ProgressRequest request) {

        long userId= getCurrentUserId();


        progressService.updateStepStatus(userId, request.getStepId(), request.getStatus());

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Progress updated successfully")
                        .data(request.getStatus().toUpperCase())
                        .status(200)
                        .build()
        );
    }

    @GetMapping("/{pathId}")
    public ResponseEntity<ApiResponse<Double>> getProgress(@PathVariable Long pathId) {
        Long userId = getCurrentUserId();
        double progress = progressService.calculateProgress(userId, pathId);

        // ✅ FIXED: Wrap in ApiResponse to match frontend userService logic
        return ResponseEntity.ok(
                ApiResponse.<Double>builder()
                        .success(true)
                        .data(progress)
                        .message("Progress fetched")
                        .status(200)
                        .build()
        );
    }

    @GetMapping("/status/{pathId}")
    public ResponseEntity<String> getStatus(@PathVariable Long pathId) {

        Long userId = getCurrentUserId(); // abhi static rakh sakta hai

        String status = progressService.getPathStatus(userId, pathId);

        return ResponseEntity.ok(status);
    }

    private Long getCurrentUserId() {
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