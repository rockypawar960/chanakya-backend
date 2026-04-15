package com.chanakya.controller.admin;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.admin.*;
import com.chanakya.service.admin.AdminRecommendationConfigService;
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

import java.util.List;

@RestController
@RequestMapping("/admin/recommendation-config")
@RequiredArgsConstructor
@Slf4j
public class AdminRecommendationConfigController {

    private final AdminRecommendationConfigService adminRecommendationConfigService;

    /**
     * GET /api/admin/recommendation-config - Get all configs
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllConfigs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bucketName") String sortBy) {
        
        log.info("[AdminRecommendationConfigController] Fetching all configs");
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            PaginatedResponse<AdminRecommendationConfigDTO> response = 
                    adminRecommendationConfigService.getAllConfigs(pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Configs retrieved successfully", response));
        } catch (Exception e) {
            log.error("[AdminRecommendationConfigController] Error fetching configs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching configs", null));
        }
    }

    /**
     * GET /api/admin/recommendation-config/active - Get active configs only
     */
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getActiveConfigs() {
        log.info("[AdminRecommendationConfigController] Fetching active configs");
        try {
            List<AdminRecommendationConfigDTO> configs = adminRecommendationConfigService.getActiveConfigs();
            return ResponseEntity.ok(new ApiResponse<>(true, "Active configs retrieved", configs));
        } catch (Exception e) {
            log.error("[AdminRecommendationConfigController] Error fetching active configs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching active configs", null));
        }
    }

    /**
     * GET /api/admin/recommendation-config/{configId} - Get config by ID
     */
    @GetMapping("/{configId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getConfigById(@PathVariable Long configId) {
        log.info("[AdminRecommendationConfigController] Fetching config ID: {}", configId);
        try {
            AdminRecommendationConfigDTO config = adminRecommendationConfigService.getConfigById(configId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Config retrieved successfully", config));
        } catch (RuntimeException e) {
            log.error("[AdminRecommendationConfigController] Config not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Config not found", null));
        } catch (Exception e) {
            log.error("[AdminRecommendationConfigController] Error fetching config", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching config", null));
        }
    }

    /**
     * GET /api/admin/recommendation-config/bucket/{bucketName} - Get by bucket name
     */
    @GetMapping("/bucket/{bucketName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getConfigByBucketName(@PathVariable String bucketName) {
        log.info("[AdminRecommendationConfigController] Fetching config for bucket: {}", bucketName);
        try {
            AdminRecommendationConfigDTO config = 
                    adminRecommendationConfigService.getConfigByBucketName(bucketName);
            return ResponseEntity.ok(new ApiResponse<>(true, "Config retrieved successfully", config));
        } catch (RuntimeException e) {
            log.error("[AdminRecommendationConfigController] Config not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Config not found", null));
        } catch (Exception e) {
            log.error("[AdminRecommendationConfigController] Error fetching config", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching config", null));
        }
    }

    /**
     * POST /api/admin/recommendation-config - Create config
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createConfig(@Valid @RequestBody CreateRecommendationConfigRequest request) {
        log.info("[AdminRecommendationConfigController] Creating config for bucket: {}", request.getBucketName());
        try {
            AdminRecommendationConfigDTO config = adminRecommendationConfigService.createConfig(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Config created successfully", config));
        } catch (RuntimeException e) {
            log.error("[AdminRecommendationConfigController] Error creating config", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("[AdminRecommendationConfigController] Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating config", null));
        }
    }

    /**
     * PUT /api/admin/recommendation-config/{configId} - Update config
     */
    @PutMapping("/{configId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateConfig(
            @PathVariable Long configId,
            @Valid @RequestBody UpdateRecommendationConfigRequest request) {
        
        log.info("[AdminRecommendationConfigController] Updating config ID: {}", configId);
        try {
            AdminRecommendationConfigDTO config = 
                    adminRecommendationConfigService.updateConfig(configId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Config updated successfully", config));
        } catch (RuntimeException e) {
            log.error("[AdminRecommendationConfigController] Config not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Config not found", null));
        } catch (Exception e) {
            log.error("[AdminRecommendationConfigController] Error updating config", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error updating config", null));
        }
    }

    /**
     * DELETE /api/admin/recommendation-config/{configId} - Delete config
     */
    @DeleteMapping("/{configId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteConfig(@PathVariable Long configId) {
        log.info("[AdminRecommendationConfigController] Deleting config ID: {}", configId);
        try {
            adminRecommendationConfigService.deleteConfig(configId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Config deleted successfully", null));
        } catch (RuntimeException e) {
            log.error("[AdminRecommendationConfigController] Config not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Config not found", null));
        } catch (Exception e) {
            log.error("[AdminRecommendationConfigController] Error deleting config", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error deleting config", null));
        }
    }
}
