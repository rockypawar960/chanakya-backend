package com.chanakya.controller.admin;

import com.chanakya.dto.admin.*;
import com.chanakya.service.admin.AdminAssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Assessment Management Controller
 * Provides endpoints for assessment CRUD operations and analytics
 * All endpoints require ADMIN role and JWT authentication
 */
@RestController
@RequestMapping("/admin/assessments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Assessment Management", description = "API endpoints for admin assessment operations")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminAssessmentController {

    private final AdminAssessmentService assessmentService;

    /**
     * Get all assessments with pagination
     * @param page page number (default: 0)
     * @param size page size (default: 10)
     * @param sortBy field to sort by (default: completedAt)
     * @param sortDirection sort direction ASC or DESC (default: DESC)
     * @return paginated list of assessments
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all assessments with pagination",
               description = "Retrieves a paginated list of all assessments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assessments retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getAllAssessments(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Sort field", example = "completedAt")
            @RequestParam(defaultValue = "completedAt") String sortBy,
            
            @Parameter(description = "Sort direction", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        log.info("GET /api/admin/assessments - page: {}, size: {}, sortBy: {}, sortDirection: {}", 
                 page, size, sortBy, sortDirection);
        
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            PaginatedResponse response = assessmentService.getAllAssessments(pageable);
            
            return ResponseEntity.ok(ApiResponseWrapper.builder()
                    .success(true)
                    .message("Assessments retrieved successfully")
                    .data(response)
                    .build());
        } catch (IllegalArgumentException e) {
            log.error("Invalid sort direction: {}", sortDirection);
            return ResponseEntity.badRequest().body(ApiResponseWrapper.builder()
                    .success(false)
                    .message("Invalid sort direction. Use ASC or DESC")
                    .build());
        } catch (Exception e) {
            log.error("Error fetching assessments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseWrapper.builder()
                    .success(false)
                    .message("Failed to fetch assessments: " + e.getMessage())
                    .build());
        }
    }

    /**
     * Get detailed assessment information
     * @param assessmentId assessment ID
     * @return detailed assessment DTO
     */
    @GetMapping("/{assessmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get assessment details",
               description = "Retrieves detailed information about a specific assessment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assessment details retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminAssessmentDetailsDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Assessment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getAssessmentDetails(
            @Parameter(description = "Assessment ID", example = "5")
            @PathVariable Long assessmentId) {
        
        log.info("GET /api/admin/assessments/{} - Assessment details", assessmentId);
        
        try {
            AdminAssessmentDetailsDTO assessment = assessmentService.getAssessmentDetails(assessmentId);
            
            return ResponseEntity.ok(ApiResponseWrapper.builder()
                    .success(true)
                    .message("Assessment details retrieved successfully")
                    .data(assessment)
                    .build());
        } catch (EntityNotFoundException e) {
            log.warn("Assessment not found: {}", assessmentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.builder()
                    .success(false)
                    .message("Assessment not found with ID: " + assessmentId)
                    .build());
        } catch (Exception e) {
            log.error("Error fetching assessment details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseWrapper.builder()
                    .success(false)
                    .message("Failed to fetch assessment details: " + e.getMessage())
                    .build());
        }
    }

    /**
     * Update assessment status (enable/disable)
     * @param request status update request
     * @return updated assessment DTO
     */
    @PatchMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update assessment status",
               description = "Enable or disable an assessment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assessment status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminAssessmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Assessment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> updateAssessmentStatus(@Valid @RequestBody UpdateAssessmentStatusRequest request) {
        
        log.info("PATCH /api/admin/assessments/status - ID: {}, isActive: {}", 
                 request.getAssessmentId(), request.getIsActive());
        
        try {
            AdminAssessmentDTO assessment = assessmentService.updateAssessmentStatus(request);
            
            return ResponseEntity.ok(ApiResponseWrapper.builder()
                    .success(true)
                    .message("Assessment status updated successfully")
                    .data(assessment)
                    .build());
        } catch (EntityNotFoundException e) {
            log.warn("Assessment not found: {}", request.getAssessmentId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.builder()
                    .success(false)
                    .message("Assessment not found with ID: " + request.getAssessmentId())
                    .build());
        } catch (Exception e) {
            log.error("Error updating assessment status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseWrapper.builder()
                    .success(false)
                    .message("Failed to update assessment status: " + e.getMessage())
                    .build());
        }
    }

    /**
     * Delete assessment
     * @param assessmentId assessment ID to delete
     */
    @DeleteMapping("/{assessmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete assessment",
               description = "Permanently delete an assessment record")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assessment deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Assessment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> deleteAssessment(
            @Parameter(description = "Assessment ID", example = "5")
            @PathVariable Long assessmentId) {
        
        log.info("DELETE /api/admin/assessments/{} - Delete assessment", assessmentId);
        
        try {
            assessmentService.deleteAssessment(assessmentId);
            
            return ResponseEntity.ok(ApiResponseWrapper.builder()
                    .success(true)
                    .message("Assessment deleted successfully")
                    .build());
        } catch (EntityNotFoundException e) {
            log.warn("Assessment not found: {}", assessmentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.builder()
                    .success(false)
                    .message("Assessment not found with ID: " + assessmentId)
                    .build());
        } catch (Exception e) {
            log.error("Error deleting assessment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseWrapper.builder()
                    .success(false)
                    .message("Failed to delete assessment: " + e.getMessage())
                    .build());
        }
    }

    /**
     * Get assessment analytics
     * @return analytics DTO with statistics
     */
    @GetMapping("/analytics/summary")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get assessment analytics",
               description = "Retrieves comprehensive assessment statistics and analytics")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminAssessmentAnalyticsDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getAssessmentAnalytics() {
        
        log.info("GET /api/admin/assessments/analytics/summary - Assessment analytics");
        
        try {
            AdminAssessmentAnalyticsDTO analytics = assessmentService.getAssessmentAnalytics();
            
            return ResponseEntity.ok(ApiResponseWrapper.builder()
                    .success(true)
                    .message("Assessment analytics retrieved successfully")
                    .data(analytics)
                    .build());
        } catch (Exception e) {
            log.error("Error fetching assessment analytics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseWrapper.builder()
                    .success(false)
                    .message("Failed to fetch assessment analytics: " + e.getMessage())
                    .build());
        }
    }

    /**
     * Generic API response wrapper
     */
    @lombok.Data
    @lombok.Builder
    private static class ApiResponseWrapper {
        private boolean success;
        private String message;
        private Object data;
    }
}
