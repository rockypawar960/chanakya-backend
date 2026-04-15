package com.chanakya.controller.admin;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.admin.*;
import com.chanakya.service.admin.AdminFeedbackService;
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

@RestController
@RequestMapping("/admin/feedback")
@RequiredArgsConstructor
@Slf4j
public class AdminFeedbackController {

    private final AdminFeedbackService adminFeedbackService;

    /**
     * GET /api/admin/feedback - Get all feedback
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllFeedback(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        
        log.info("[AdminFeedbackController] Fetching all feedback");
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
            PaginatedResponse<AdminFeedbackDTO> response = adminFeedbackService.getAllFeedback(pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Feedback retrieved successfully", response));
        } catch (Exception e) {
            log.error("[AdminFeedbackController] Error fetching feedback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching feedback", null));
        }
    }

    /**
     * GET /api/admin/feedback/{feedbackId} - Get feedback by ID
     */
    @GetMapping("/{feedbackId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getFeedbackById(@PathVariable Long feedbackId) {
        log.info("[AdminFeedbackController] Fetching feedback ID: {}", feedbackId);
        try {
            AdminFeedbackDTO feedback = adminFeedbackService.getFeedbackById(feedbackId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Feedback retrieved successfully", feedback));
        } catch (RuntimeException e) {
            log.error("[AdminFeedbackController] Feedback not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Feedback not found", null));
        } catch (Exception e) {
            log.error("[AdminFeedbackController] Error fetching feedback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching feedback", null));
        }
    }

    /**
     * GET /api/admin/feedback/status/{status} - Get feedback by status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getFeedbackByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("[AdminFeedbackController] Fetching feedback by status: {}", status);
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginatedResponse<AdminFeedbackDTO> response = 
                    adminFeedbackService.getFeedbackByStatus(status, pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Feedback retrieved successfully", response));
        } catch (Exception e) {
            log.error("[AdminFeedbackController] Error fetching feedback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching feedback", null));
        }
    }

    /**
     * GET /api/admin/feedback/type/{type} - Get feedback by type
     */
    @GetMapping("/type/{feedbackType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getFeedbackByType(
            @PathVariable String feedbackType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("[AdminFeedbackController] Fetching feedback by type: {}", feedbackType);
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginatedResponse<AdminFeedbackDTO> response = 
                    adminFeedbackService.getFeedbackByType(feedbackType, pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Feedback retrieved successfully", response));
        } catch (Exception e) {
            log.error("[AdminFeedbackController] Error fetching feedback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching feedback", null));
        }
    }

    /**
     * GET /api/admin/feedback/user/{userId} - Get feedback by user
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getFeedbackByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("[AdminFeedbackController] Fetching feedback for user ID: {}", userId);
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginatedResponse<AdminFeedbackDTO> response = 
                    adminFeedbackService.getFeedbackByUser(userId, pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Feedback retrieved successfully", response));
        } catch (Exception e) {
            log.error("[AdminFeedbackController] Error fetching feedback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching feedback", null));
        }
    }

    /**
     * GET /api/admin/feedback/statistics - Get feedback statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getFeedbackStatistics() {
        log.info("[AdminFeedbackController] Fetching feedback statistics");
        try {
            FeedbackStatisticsDTO stats = adminFeedbackService.getFeedbackStatistics();
            return ResponseEntity.ok(new ApiResponse<>(true, "Statistics retrieved successfully", stats));
        } catch (Exception e) {
            log.error("[AdminFeedbackController] Error fetching statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching statistics", null));
        }
    }

    /**
     * POST /api/admin/feedback - Create feedback (user endpoint)
     */
    @PostMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> createFeedback(
            @PathVariable Long userId,
            @Valid @RequestBody CreateFeedbackRequest request) {
        
        log.info("[AdminFeedbackController] Creating feedback for user ID: {}", userId);
        try {
            AdminFeedbackDTO feedback = adminFeedbackService.createFeedback(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Feedback created successfully", feedback));
        } catch (RuntimeException e) {
            log.error("[AdminFeedbackController] Error creating feedback", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("[AdminFeedbackController] Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating feedback", null));
        }
    }

    /**
     * PUT /api/admin/feedback/{feedbackId} - Update feedback response (admin only)
     */
    @PutMapping("/{feedbackId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateFeedbackResponse(
            @PathVariable Long feedbackId,
            @Valid @RequestBody UpdateFeedbackResponseRequest request) {
        
        log.info("[AdminFeedbackController] Updating feedback ID: {}", feedbackId);
        try {
            AdminFeedbackDTO feedback = adminFeedbackService.updateFeedbackResponse(feedbackId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Feedback updated successfully", feedback));
        } catch (RuntimeException e) {
            log.error("[AdminFeedbackController] Feedback not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Feedback not found", null));
        } catch (Exception e) {
            log.error("[AdminFeedbackController] Error updating feedback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error updating feedback", null));
        }
    }

    /**
     * DELETE /api/admin/feedback/{feedbackId} - Delete feedback (admin only)
     */
    @DeleteMapping("/{feedbackId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long feedbackId) {
        log.info("[AdminFeedbackController] Deleting feedback ID: {}", feedbackId);
        try {
            adminFeedbackService.deleteFeedback(feedbackId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Feedback deleted successfully", null));
        } catch (RuntimeException e) {
            log.error("[AdminFeedbackController] Feedback not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Feedback not found", null));
        } catch (Exception e) {
            log.error("[AdminFeedbackController] Error deleting feedback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error deleting feedback", null));
        }
    }
}
