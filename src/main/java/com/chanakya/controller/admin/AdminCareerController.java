package com.chanakya.controller.admin;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.admin.*;
import com.chanakya.service.admin.AdminCareerService;
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
@RequestMapping("/admin/careers")
@RequiredArgsConstructor
@Slf4j
public class AdminCareerController {

    private final AdminCareerService adminCareerService;

    /**
     * GET /api/admin/careers - Get all careers with pagination
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCareers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        
        log.info("[AdminCareerController] Fetching all careers - page: {}, size: {}", page, size);
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
            PaginatedResponse<AdminCareerDTO> response = adminCareerService.getAllCareers(pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Careers retrieved successfully", response));
        } catch (Exception e) {
            log.error("[AdminCareerController] Error fetching careers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching careers: " + e.getMessage(), null));
        }
    }

    /**
     * GET /api/admin/careers/{careerId} - Get career details
     */
    @GetMapping("/{careerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCareerDetails(@PathVariable Long careerId) {
        log.info("[AdminCareerController] Fetching career details for ID: {}", careerId);
        try {
            AdminCareerDetailsDTO career = adminCareerService.getCareerDetails(careerId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Career details retrieved", career));
        } catch (RuntimeException e) {
            log.error("[AdminCareerController] Career not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Career not found", null));
        } catch (Exception e) {
            log.error("[AdminCareerController] Error fetching career details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching career details", null));
        }
    }

    /**
     * POST /api/admin/careers - Create new career
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCareer(@Valid @RequestBody CreateCareerRequest request) {
        log.info("[AdminCareerController] Creating new career: {}", request.getName());
        try {
            AdminCareerDTO career = adminCareerService.createCareer(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Career created successfully", career));
        } catch (RuntimeException e) {
            log.error("[AdminCareerController] Error creating career", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("[AdminCareerController] Unexpected error creating career", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating career", null));
        }
    }

    /**
     * PUT /api/admin/careers/{careerId} - Update career
     */
    @PutMapping("/{careerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCareer(
            @PathVariable Long careerId,
            @Valid @RequestBody UpdateCareerRequest request) {
        log.info("[AdminCareerController] Updating career with ID: {}", careerId);
        try {
            AdminCareerDTO career = adminCareerService.updateCareer(careerId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Career updated successfully", career));
        } catch (RuntimeException e) {
            log.error("[AdminCareerController] Error updating career", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("[AdminCareerController] Unexpected error updating career", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error updating career", null));
        }
    }

    /**
     * DELETE /api/admin/careers/{careerId} - Delete career
     */
    @DeleteMapping("/{careerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCareer(@PathVariable Long careerId) {
        log.info("[AdminCareerController] Deleting career with ID: {}", careerId);
        try {
            adminCareerService.deleteCareer(careerId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Career deleted successfully", null));
        } catch (RuntimeException e) {
            log.error("[AdminCareerController] Career not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Career not found", null));
        } catch (Exception e) {
            log.error("[AdminCareerController] Error deleting career", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error deleting career", null));
        }
    }
}
