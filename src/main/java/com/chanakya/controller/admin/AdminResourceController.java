package com.chanakya.controller.admin;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.admin.*;
import com.chanakya.service.admin.AdminResourceService;
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
@RequestMapping("/admin/resources")
@RequiredArgsConstructor
@Slf4j
public class AdminResourceController {

    private final AdminResourceService adminResourceService;

    /**
     * GET /api/admin/resources - Get all resources
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllResources(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        
        log.info("[AdminResourceController] Fetching all resources");
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
            PaginatedResponse<AdminResourceDTO> response = adminResourceService.getAllResources(pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resources retrieved successfully", response));
        } catch (Exception e) {
            log.error("[AdminResourceController] Error fetching resources", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching resources", null));
        }
    }

    /**
     * GET /api/admin/resources/{resourceId} - Get resource by ID
     */
    @GetMapping("/{resourceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getResourceById(@PathVariable Long resourceId) {
        log.info("[AdminResourceController] Fetching resource ID: {}", resourceId);
        try {
            AdminResourceDTO resource = adminResourceService.getResourceById(resourceId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resource retrieved successfully", resource));
        } catch (RuntimeException e) {
            log.error("[AdminResourceController] Resource not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Resource not found", null));
        } catch (Exception e) {
            log.error("[AdminResourceController] Error fetching resource", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching resource", null));
        }
    }

    /**
     * GET /api/admin/resources/career/{careerId} - Get resources by career
     */
    @GetMapping("/career/{careerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getResourcesByCareer(
            @PathVariable Long careerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("[AdminResourceController] Fetching resources for career ID: {}", careerId);
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginatedResponse<AdminResourceDTO> response = 
                    adminResourceService.getResourcesByCareer(careerId, pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resources retrieved successfully", response));
        } catch (RuntimeException e) {
            log.error("[AdminResourceController] Career not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Career not found", null));
        } catch (Exception e) {
            log.error("[AdminResourceController] Error fetching resources", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching resources", null));
        }
    }

    /**
     * GET /api/admin/resources/type/{type} - Get resources by type
     */
    @GetMapping("/type/{resourceType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getResourcesByType(
            @PathVariable String resourceType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("[AdminResourceController] Fetching resources by type: {}", resourceType);
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginatedResponse<AdminResourceDTO> response = 
                    adminResourceService.getResourcesByType(resourceType, pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resources retrieved successfully", response));
        } catch (Exception e) {
            log.error("[AdminResourceController] Error fetching resources", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching resources", null));
        }
    }

    /**
     * POST /api/admin/resources - Create resource
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createResource(@Valid @RequestBody CreateResourceRequest request) {
        log.info("[AdminResourceController] Creating resource: {}", request.getTitle());
        try {
            AdminResourceDTO resource = adminResourceService.createResource(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Resource created successfully", resource));
        } catch (RuntimeException e) {
            log.error("[AdminResourceController] Error creating resource", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("[AdminResourceController] Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating resource", null));
        }
    }

    /**
     * PUT /api/admin/resources/{resourceId} - Update resource
     */
    @PutMapping("/{resourceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateResource(
            @PathVariable Long resourceId,
            @Valid @RequestBody UpdateResourceRequest request) {
        
        log.info("[AdminResourceController] Updating resource ID: {}", resourceId);
        try {
            AdminResourceDTO resource = adminResourceService.updateResource(resourceId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resource updated successfully", resource));
        } catch (RuntimeException e) {
            log.error("[AdminResourceController] Resource not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Resource not found", null));
        } catch (Exception e) {
            log.error("[AdminResourceController] Error updating resource", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error updating resource", null));
        }
    }

    /**
     * DELETE /api/admin/resources/{resourceId} - Delete resource
     */
    @DeleteMapping("/{resourceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteResource(@PathVariable Long resourceId) {
        log.info("[AdminResourceController] Deleting resource ID: {}", resourceId);
        try {
            adminResourceService.deleteResource(resourceId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resource deleted successfully", null));
        } catch (RuntimeException e) {
            log.error("[AdminResourceController] Resource not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Resource not found", null));
        } catch (Exception e) {
            log.error("[AdminResourceController] Error deleting resource", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error deleting resource", null));
        }
    }
}
