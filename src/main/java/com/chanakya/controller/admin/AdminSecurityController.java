package com.chanakya.controller.admin;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.admin.*;
import com.chanakya.service.admin.AdminSecurityService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/admin/security")
@RequiredArgsConstructor
@Slf4j
public class AdminSecurityController {

    private final AdminSecurityService adminSecurityService;

    /**
     * GET /api/admin/security/activity-logs - Get all activity logs
     */
    @GetMapping("/activity-logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getActivityLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        
        log.info("[AdminSecurityController] Fetching activity logs");
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
            PaginatedResponse<AdminActivityLogDTO> response = 
                    adminSecurityService.getAllActivityLogs(pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Activity logs retrieved successfully", response));
        } catch (Exception e) {
            log.error("[AdminSecurityController] Error fetching activity logs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching activity logs", null));
        }
    }

    /**
     * GET /api/admin/security/activity-logs/admin/{adminId} - Get logs by admin
     */
    @GetMapping("/activity-logs/admin/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getActivityLogsByAdmin(
            @PathVariable Long adminId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("[AdminSecurityController] Fetching logs for admin ID: {}", adminId);
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginatedResponse<AdminActivityLogDTO> response = 
                    adminSecurityService.getActivityLogsByAdmin(adminId, pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Activity logs retrieved", response));
        } catch (Exception e) {
            log.error("[AdminSecurityController] Error fetching logs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching logs", null));
        }
    }

    /**
     * GET /api/admin/security/activity-logs/action/{action} - Get logs by action
     */
    @GetMapping("/activity-logs/action/{action}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getActivityLogsByAction(
            @PathVariable String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("[AdminSecurityController] Fetching logs by action: {}", action);
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginatedResponse<AdminActivityLogDTO> response = 
                    adminSecurityService.getActivityLogsByAction(action, pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Activity logs retrieved", response));
        } catch (Exception e) {
            log.error("[AdminSecurityController] Error fetching logs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching logs", null));
        }
    }

    /**
     * GET /api/admin/security/settings - Get security settings
     */
    @GetMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSecuritySettings() {
        log.info("[AdminSecurityController] Fetching security settings");
        try {
            AdminSecuritySettingsDTO settings = adminSecurityService.getSecuritySettings();
            return ResponseEntity.ok(new ApiResponse<>(true, "Security settings retrieved", settings));
        } catch (Exception e) {
            log.error("[AdminSecurityController] Error fetching settings", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching settings", null));
        }
    }

    /**
     * POST /api/admin/security/password-reset/{userId} - Reset user password
     */
    @PostMapping("/password-reset/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resetUserPassword(
            @PathVariable Long userId,
            @Valid @RequestBody PasswordResetRequest request,
            HttpServletRequest httpRequest) {
        
        log.info("[AdminSecurityController] Password reset requested for user ID: {}", userId);
        try {
            String ipAddress = getClientIpAddress(httpRequest);
            // Would need authenticated admin ID - this is a placeholder
            Long adminId = 1L; // Replace with actual authenticated admin
            
            adminSecurityService.resetUserPassword(adminId, userId, request.getNewPassword(), ipAddress);
            return ResponseEntity.ok(new ApiResponse<>(true, "Password reset successfully", null));
        } catch (RuntimeException e) {
            log.error("[AdminSecurityController] Error resetting password", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("[AdminSecurityController] Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error resetting password", null));
        }
    }

    /**
     * GET /api/admin/security/jwt/verify - Verify JWT integration
     */
    @GetMapping("/jwt/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> verifyJWTIntegration() {
        log.info("[AdminSecurityController] Verifying JWT integration");
        try {
            boolean isVerified = adminSecurityService.verifyJWTIntegration();
            return ResponseEntity.ok(new ApiResponse<>(true, 
                    isVerified ? "JWT integration verified" : "JWT integration failed",
                    isVerified));
        } catch (Exception e) {
            log.error("[AdminSecurityController] Error verifying JWT", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error verifying JWT", false));
        }
    }

    /**
     * Helper: Get client IP address
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
        for (String header : headers) {
            String ipAddress = request.getHeader(header);
            if (ipAddress != null && !ipAddress.isEmpty() && !"unknown".equalsIgnoreCase(ipAddress)) {
                return ipAddress.split(",")[0];
            }
        }
        return request.getRemoteAddr();
    }
}
