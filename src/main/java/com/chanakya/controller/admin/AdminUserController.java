package com.chanakya.controller.admin;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.admin.*;
import com.chanakya.service.admin.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin User Management", description = "Endpoints for managing users")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class AdminUserController {

    private final AdminUserService adminUserService;

    // ✅ GET ALL USERS
    @GetMapping
    @Operation(summary = "Get all users with pagination")
    public ResponseEntity<ApiResponse<PaginatedResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) {

        log.info("Fetching users: page={}, size={}, sortBy={}, direction={}",
                page, size, sortBy, sortDirection);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        PaginatedResponse response = adminUserService.getAllUsers(pageable);

        return ResponseEntity.ok(
                ApiResponse.<PaginatedResponse>builder()
                        .success(true)
                        .message("Users retrieved successfully")
                        .data(response)
                        .status(200)
                        .build()
        );
    }

    // ✅ GET USER DETAILS
    @GetMapping("/{userId}")
    @Operation(summary = "Get user details")
    public ResponseEntity<ApiResponse<AdminUserDetailsDTO>> getUserDetails(
            @PathVariable Long userId) {

        log.info("Fetching user details for id={}", userId);

        AdminUserDetailsDTO user = adminUserService.getUserDetailsWithAssessmentHistory(userId);

        return ResponseEntity.ok(
                ApiResponse.<AdminUserDetailsDTO>builder()
                        .success(true)
                        .message("User details retrieved successfully")
                        .data(user)
                        .status(200)
                        .build()
        );
    }

    // ✅ UPDATE USER STATUS
    @PatchMapping("/status")
    @Operation(summary = "Update user status")
    public ResponseEntity<ApiResponse<AdminUserDTO>> updateUserStatus(
            @Valid @RequestBody UpdateUserStatusRequest request) {

        log.info("Updating status for userId={}, active={}",
                request.getUserId(), request.getIsActive());

        AdminUserDTO updatedUser = adminUserService.updateUserStatus(
                request.getUserId(),
                request.getIsActive()
        );

        String message = request.getIsActive() ?
                "User enabled successfully" : "User disabled successfully";

        return ResponseEntity.ok(
                ApiResponse.<AdminUserDTO>builder()
                        .success(true)
                        .message(message)
                        .data(updatedUser)
                        .status(200)
                        .build()
        );
    }

    // ✅ UPDATE USER ROLE
    @PutMapping("/role")
    @Operation(summary = "Update user role")
    public ResponseEntity<ApiResponse<AdminUserDTO>> updateUserRole(
            @Valid @RequestBody UpdateUserRoleRequest request) {

        log.info("Updating role for userId={}, role={}",
                request.getUserId(), request.getRoleName());

        AdminUserDTO updatedUser = adminUserService.updateUserRole(
                request.getUserId(),
                request.getRoleName()
        );

        return ResponseEntity.ok(
                ApiResponse.<AdminUserDTO>builder()
                        .success(true)
                        .message("User role updated successfully")
                        .data(updatedUser)
                        .status(200)
                        .build()
        );
    }

    // ✅ DELETE USER
    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId) {

        log.info("Deleting user id={}", userId);

        adminUserService.deleteUser(userId);

        return ResponseEntity.noContent().build(); // ✅ BEST PRACTICE
    }
}