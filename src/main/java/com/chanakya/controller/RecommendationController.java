package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.RecommendationDTO;
import com.chanakya.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendation", description = "Career recommendation endpoints")
@Slf4j
public class RecommendationController {

    private final RecommendationService recommendationService;

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
        // TODO: Implement proper user ID extraction from SecurityContext
        return 1L; // Temporary
    }
}