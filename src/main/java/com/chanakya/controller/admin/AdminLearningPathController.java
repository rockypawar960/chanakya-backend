package com.chanakya.controller.admin;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.admin.*;
import com.chanakya.service.admin.AdminLearningPathService;
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
@RequestMapping("/api/admin/learning-paths")
@RequiredArgsConstructor
@Slf4j
public class AdminLearningPathController {

    private final AdminLearningPathService adminLearningPathService;

    /**
     * GET /api/admin/learning-paths - Get all learning paths
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllLearningPaths(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        
        log.info("[AdminLearningPathController] Fetching all learning paths");
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
            PaginatedResponse<AdminLearningPathDTO> response = adminLearningPathService.getAllLearningPaths(pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Learning paths retrieved successfully", response));
        } catch (Exception e) {
            log.error("[AdminLearningPathController] Error fetching learning paths", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching learning paths", null));
        }
    }

    /**
     * GET /api/admin/learning-paths/{pathId} - Get learning path details
     */
    @GetMapping("/{pathId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getLearningPathDetails(@PathVariable Long pathId) {
        log.info("[AdminLearningPathController] Fetching learning path details for ID: {}", pathId);
        try {
            AdminLearningPathDetailsDTO path = adminLearningPathService.getLearningPathDetails(pathId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Learning path details retrieved", path));
        } catch (RuntimeException e) {
            log.error("[AdminLearningPathController] Learning path not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Learning path not found", null));
        } catch (Exception e) {
            log.error("[AdminLearningPathController] Error fetching learning path", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching learning path", null));
        }
    }

    /**
     * GET /api/admin/learning-paths/career/{careerId} - Get paths by career
     */
    @GetMapping("/career/{careerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getLearningPathsByCareer(
            @PathVariable Long careerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("[AdminLearningPathController] Fetching learning paths for career ID: {}", careerId);
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginatedResponse<AdminLearningPathDTO> response = 
                    adminLearningPathService.getLearningPathsByCareer(careerId, pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Learning paths retrieved", response));
        } catch (RuntimeException e) {
            log.error("[AdminLearningPathController] Career not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Career not found", null));
        } catch (Exception e) {
            log.error("[AdminLearningPathController] Error fetching learning paths", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching learning paths", null));
        }
    }

    /**
     * POST /api/admin/learning-paths - Create learning path
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createLearningPath(@Valid @RequestBody CreateLearningPathRequest request) {
        log.info("[AdminLearningPathController] Creating learning path: {}", request.getPathName());
        try {
            AdminLearningPathDTO path = adminLearningPathService.createLearningPath(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Learning path created successfully", path));
        } catch (RuntimeException e) {
            log.error("[AdminLearningPathController] Error creating learning path", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("[AdminLearningPathController] Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating learning path", null));
        }
    }

    /**
     * PUT /api/admin/learning-paths/{pathId} - Update learning path
     */
    @PutMapping("/{pathId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateLearningPath(
            @PathVariable Long pathId,
            @Valid @RequestBody UpdateLearningPathRequest request) {
        
        log.info("[AdminLearningPathController] Updating learning path ID: {}", pathId);
        try {
            AdminLearningPathDTO path = adminLearningPathService.updateLearningPath(pathId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Learning path updated successfully", path));
        } catch (RuntimeException e) {
            log.error("[AdminLearningPathController] Learning path not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Learning path not found", null));
        } catch (Exception e) {
            log.error("[AdminLearningPathController] Error updating learning path", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error updating learning path", null));
        }
    }

    /**
     * DELETE /api/admin/learning-paths/{pathId} - Delete learning path
     */
    @DeleteMapping("/{pathId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteLearningPath(@PathVariable Long pathId) {
        log.info("[AdminLearningPathController] Deleting learning path ID: {}", pathId);
        try {
            adminLearningPathService.deleteLearningPath(pathId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Learning path deleted successfully", null));
        } catch (RuntimeException e) {
            log.error("[AdminLearningPathController] Learning path not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Learning path not found", null));
        } catch (Exception e) {
            log.error("[AdminLearningPathController] Error deleting learning path", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error deleting learning path", null));
        }
    }
}
