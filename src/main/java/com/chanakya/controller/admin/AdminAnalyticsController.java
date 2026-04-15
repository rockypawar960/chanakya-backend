package com.chanakya.controller.admin;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.admin.*;
import com.chanakya.service.admin.AdminAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/analytics")
@RequiredArgsConstructor
@Slf4j
public class AdminAnalyticsController {

    private final AdminAnalyticsService adminAnalyticsService;

    @GetMapping("/comprehensive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminAnalyticsDTO>> getComprehensiveAnalytics() {

        log.info("Fetching comprehensive analytics");

        AdminAnalyticsDTO analytics = adminAnalyticsService.getComprehensiveAnalytics();

        return ResponseEntity.ok(
                ApiResponse.<AdminAnalyticsDTO>builder()
                        .success(true)
                        .message("Analytics retrieved successfully")
                        .data(analytics)
                        .status(200)
                        .build()
        );
    }

    @GetMapping("/top-careers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CareerInterestDTO>>> getTopCareers() {

        log.info("Fetching top careers");

        List<CareerInterestDTO> topCareers = adminAnalyticsService.getTopCareers();

        return ResponseEntity.ok(
                ApiResponse.<List<CareerInterestDTO>>builder()
                        .success(true)
                        .message("Top careers retrieved")
                        .data(topCareers)
                        .status(200)
                        .build()
        );
    }

    @GetMapping("/user-distribution")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUserInterestDistribution() {

        log.info("Fetching user interest distribution");

        Map<String, Long> distribution = adminAnalyticsService.getUserInterestDistribution();

        return ResponseEntity.ok(
                ApiResponse.<Map<String, Long>>builder()
                        .success(true)
                        .message("User distribution retrieved")
                        .data(distribution)
                        .status(200)
                        .build()
        );
    }

    @GetMapping("/drop-off-rate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDropOffRate() {

        log.info("Fetching drop-off rate");

        Double dropOffRate = adminAnalyticsService.calculateDropOffRate();

        Map<String, Object> response = Map.of(
                "dropOffRate", dropOffRate,
                "unit", "percentage"
        );

        return ResponseEntity.ok(
                ApiResponse.<Map<String, Object>>builder()
                        .success(true)
                        .message("Drop-off rate retrieved")
                        .data(response)
                        .status(200)
                        .build()
        );
    }

    @GetMapping("/skill-demand")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getSkillDemand() {

        log.info("Fetching skill demand");

        Map<String, Long> skillDemand = adminAnalyticsService.getSkillDemand();

        return ResponseEntity.ok(
                ApiResponse.<Map<String, Long>>builder()
                        .success(true)
                        .message("Skill demand retrieved")
                        .data(skillDemand)
                        .status(200)
                        .build()
        );
    }

    @GetMapping("/user-acquisition-trend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUserAcquisitionTrend() {

        log.info("Fetching user acquisition trend");

        Map<String, Long> trend = adminAnalyticsService.getUserAcquisitionTrend();

        return ResponseEntity.ok(
                ApiResponse.<Map<String, Long>>builder()
                        .success(true)
                        .message("User trend retrieved")
                        .data(trend)
                        .status(200)
                        .build()
        );
    }

    @GetMapping("/assessment-completion-trend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getAssessmentCompletionTrend() {

        log.info("Fetching assessment completion trend");

        Map<String, Long> trend = adminAnalyticsService.getAssessmentCompletionTrend();

        return ResponseEntity.ok(
                ApiResponse.<Map<String, Long>>builder()
                        .success(true)
                        .message("Assessment trend retrieved")
                        .data(trend)
                        .status(200)
                        .build()
        );
    }
}