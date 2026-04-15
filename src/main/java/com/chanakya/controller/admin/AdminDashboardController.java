package com.chanakya.controller.admin;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.admin.AdminDashboardDTO;
import com.chanakya.service.admin.AdminDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Dashboard", description = "Admin dashboard statistics and analytics endpoints")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping
    @Operation(summary = "Get dashboard statistics",
            description = "Fetch complete dashboard statistics including users, assessments, careers, and trends")
    public ResponseEntity<?> getDashboardStats() {
        log.info("Admin requesting dashboard statistics");

        try {
            AdminDashboardDTO dashboardStats = adminDashboardService.getDashboardStats();

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .success(true)
                            .message("Dashboard statistics retrieved successfully")
                            .data(dashboardStats)
                            .status(HttpStatus.OK.value())
                            .build()  // ✅ timestamp automatically set in constructor
            );
        } catch (Exception e) {
            log.error("Error fetching dashboard statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Failed to fetch dashboard statistics: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build()
            );
        }
    }

    @GetMapping("/users")
    @Operation(summary = "Get user statistics",
            description = "Get user count statistics including daily and weekly active users")
    public ResponseEntity<?> getUserStats() {
        log.info("Admin requesting user statistics");

        try {
            AdminDashboardDTO.DailyTrendDTO userStats = AdminDashboardDTO.DailyTrendDTO.builder()
                    .date("Total Users")
                    .count(adminDashboardService.getTotalUsers())
                    .build();

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .success(true)
                            .message("User statistics retrieved successfully")
                            .data(userStats)
                            .status(HttpStatus.OK.value())
                            .build()
            );
        } catch (Exception e) {
            log.error("Error fetching user statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Failed to fetch user statistics")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build()
            );
        }
    }

    @GetMapping("/assessments")
    @Operation(summary = "Get assessment statistics",
            description = "Get assessment count and completion rate statistics")
    public ResponseEntity<?> getAssessmentStats() {
        log.info("Admin requesting assessment statistics");

        try {
            // ✅ Java 8 compatible - HashMap instead of Map.of()
            Map<String, Object> assessmentStats = new HashMap<>();
            assessmentStats.put("totalAssessments", adminDashboardService.getTotalAssessments());
            assessmentStats.put("completionRate", adminDashboardService.getAssessmentCompletionRate());

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .success(true)
                            .message("Assessment statistics retrieved successfully")
                            .data(assessmentStats)
                            .status(HttpStatus.OK.value())
                            .build()
            );
        } catch (Exception e) {
            log.error("Error fetching assessment statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Failed to fetch assessment statistics")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build()
            );
        }
    }

    @GetMapping("/careers/top")
    @Operation(summary = "Get top careers",
            description = "Get the top 5 most popular careers with recommendation counts")
    public ResponseEntity<?> getTopCareers() {
        log.info("Admin requesting top careers");

        try {
            Object topCareers = adminDashboardService.getTopCareers();

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .success(true)
                            .message("Top careers retrieved successfully")
                            .data(topCareers)
                            .status(HttpStatus.OK.value())
                            .build()
            );
        } catch (Exception e) {
            log.error("Error fetching top careers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Failed to fetch top careers")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build()
            );
        }
    }

    @GetMapping("/careers/distribution")
    @Operation(summary = "Get career popularity distribution",
            description = "Get all careers with their popularity scores")
    public ResponseEntity<?> getCareerDistribution() {
        log.info("Admin requesting career distribution");

        try {
            Object distribution = adminDashboardService.getCareerPopularityDistribution();

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .success(true)
                            .message("Career distribution retrieved successfully")
                            .data(distribution)
                            .status(HttpStatus.OK.value())
                            .build()
            );
        } catch (Exception e) {
            log.error("Error fetching career distribution", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Failed to fetch career distribution")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build()
            );
        }
    }

    @GetMapping("/trends/registrations")
    @Operation(summary = "Get registration trend",
            description = "Get user registration trend data for the last 7 days")
    public ResponseEntity<?> getRegistrationTrend() {
        log.info("Admin requesting registration trend");

        try {
            Object trend = adminDashboardService.getUserRegistrationTrend();

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .success(true)
                            .message("Registration trend retrieved successfully")
                            .data(trend)
                            .status(HttpStatus.OK.value())
                            .build()
            );
        } catch (Exception e) {
            log.error("Error fetching registration trend", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Failed to fetch registration trend")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build()
            );
        }
    }

    @GetMapping("/trends/assessments")
    @Operation(summary = "Get assessment trend",
            description = "Get assessment completion trend data for the last 7 days")
    public ResponseEntity<?> getAssessmentTrend() {
        log.info("Admin requesting assessment trend");

        try {
            Object trend = adminDashboardService.getAssessmentCompletionTrend();

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .success(true)
                            .message("Assessment trend retrieved successfully")
                            .data(trend)
                            .status(HttpStatus.OK.value())
                            .build()
            );
        } catch (Exception e) {
            log.error("Error fetching assessment trend", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Failed to fetch assessment trend")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build()
            );
        }
    }

    @GetMapping("/users/active/daily")
    @Operation(summary = "Get daily active users",
            description = "Get count of users who logged in within the last 24 hours")
    public ResponseEntity<?> getDailyActiveUsers() {
        log.info("Admin requesting daily active users count");

        try {
            long activeUsers = adminDashboardService.getActiveUsersDaily();

            // ✅ Java 8 compatible
            Map<String, Object> response = new HashMap<>();
            response.put("activeUsersDaily", activeUsers);

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .success(true)
                            .message("Daily active users retrieved successfully")
                            .data(response)
                            .status(HttpStatus.OK.value())
                            .build()
            );
        } catch (Exception e) {
            log.error("Error fetching daily active users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Failed to fetch daily active users")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build()
            );
        }
    }

    @GetMapping("/users/active/weekly")
    @Operation(summary = "Get weekly active users",
            description = "Get count of users who logged in within the last 7 days")
    public ResponseEntity<?> getWeeklyActiveUsers() {
        log.info("Admin requesting weekly active users count");

        try {
            long activeUsers = adminDashboardService.getActiveUsersWeekly();

            // ✅ Java 8 compatible
            Map<String, Object> response = new HashMap<>();
            response.put("activeUsersWeekly", activeUsers);

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .success(true)
                            .message("Weekly active users retrieved successfully")
                            .data(response)
                            .status(HttpStatus.OK.value())
                            .build()
            );
        } catch (Exception e) {
            log.error("Error fetching weekly active users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .success(false)
                            .message("Failed to fetch weekly active users")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build()
            );
        }
    }
}