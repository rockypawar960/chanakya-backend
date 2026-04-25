package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.RecommendationDTO;
import com.chanakya.repository.UserRepository;
import com.chanakya.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendation", description = "Career recommendation endpoints")
@Slf4j
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

    @GetMapping("/assessment/{assessmentId}")
    @Operation(summary = "Get recommendations by assessment", description = "Get recommendations based on assessment results")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ApiResponse<List<RecommendationDTO>>> getRecommendationsByAssessment(
            @PathVariable Long assessmentId) {

        log.info("Fetching recommendations for assessment id: {}", assessmentId);
        List<RecommendationDTO> recommendations = recommendationService.getRecommendationsByAssessmentId(assessmentId);

        return ResponseEntity.ok(
                ApiResponse.<List<RecommendationDTO>>builder()
                        .success(true)
                        .message("Recommendations retrieved successfully")
                        .data(recommendations)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get recommendations for user", description = "Get all career recommendations for a specific user")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ApiResponse<List<RecommendationDTO>>> getRecommendationsByUserId(
            @PathVariable Long userId) {

        log.info("Fetching recommendations for user id: {}", userId);
        List<RecommendationDTO> recommendations = recommendationService.getRecommendationsByUserId(userId);

        return ResponseEntity.ok(
                ApiResponse.<List<RecommendationDTO>>builder()
                        .success(true)
                        .message(recommendations.isEmpty() ? "No recommendations found" : "Recommendations retrieved successfully")
                        .data(recommendations)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/my")
    @Operation(summary = "Get my recommendations", description = "Get recommendations for currently logged in user")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ApiResponse<List<RecommendationDTO>>> getMyRecommendations() {

        Long userId = getCurrentUserId();
        log.info("Fetching recommendations for current user id: {}", userId);

        List<RecommendationDTO> recommendations = recommendationService.getRecommendationsByUserId(userId);

        return ResponseEntity.ok(
                ApiResponse.<List<RecommendationDTO>>builder()
                        .success(true)
                        .message("Recommendations retrieved successfully")
                        .data(recommendations)
                        .status(HttpStatus.OK.value())
                        .build()
        );
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