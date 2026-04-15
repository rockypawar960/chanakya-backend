package com.chanakya.service.admin;

import com.chanakya.entity.AdminActivityLog;
import com.chanakya.entity.User;
import com.chanakya.dto.admin.*;
import com.chanakya.repository.AdminActivityLogRepository;
import com.chanakya.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminSecurityService {

    private final AdminActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Get all activity logs with pagination
     */
    @Transactional(readOnly = true)
    public PaginatedResponse<AdminActivityLogDTO> getAllActivityLogs(Pageable pageable) {
        log.info("[AdminSecurityService] Fetching all activity logs");
        Page<AdminActivityLog> logsPage = activityLogRepository.findAll(pageable);
        return PaginatedResponse.<AdminActivityLogDTO>builder()
                .content(logsPage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(logsPage.getNumber())
                .pageSize(logsPage.getSize())
                .totalElements(logsPage.getTotalElements())
                .totalPages(logsPage.getTotalPages())
                .isLast(logsPage.isLast())
                .build();
    }

    /**
     * Get activity logs by admin
     */
    @Transactional(readOnly = true)
    public PaginatedResponse<AdminActivityLogDTO> getActivityLogsByAdmin(Long adminId, Pageable pageable) {
        log.info("[AdminSecurityService] Fetching logs for admin ID: {}", adminId);
        Page<AdminActivityLog> logsPage = activityLogRepository.findByAdminId(adminId, pageable);
        return PaginatedResponse.<AdminActivityLogDTO>builder()
                .content(logsPage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(logsPage.getNumber())
                .pageSize(logsPage.getSize())
                .totalElements(logsPage.getTotalElements())
                .totalPages(logsPage.getTotalPages())
                .isLast(logsPage.isLast())
                .build();
    }

    /**
     * Get activity logs by action
     */
    @Transactional(readOnly = true)
    public PaginatedResponse<AdminActivityLogDTO> getActivityLogsByAction(String action, Pageable pageable) {
        log.info("[AdminSecurityService] Fetching logs by action: {}", action);
        Page<AdminActivityLog> logsPage = activityLogRepository.findByAction(action, pageable);
        return PaginatedResponse.<AdminActivityLogDTO>builder()
                .content(logsPage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(logsPage.getNumber())
                .pageSize(logsPage.getSize())
                .totalElements(logsPage.getTotalElements())
                .totalPages(logsPage.getTotalPages())
                .isLast(logsPage.isLast())
                .build();
    }

    /**
     * Log admin activity
     */
    public void logAdminActivity(Long adminId, String action, String entityType, Long entityId, String details, String ipAddress) {
        log.info("[AdminSecurityService] Logging activity for admin ID: {} - Action: {}", adminId, action);
        
        User admin = userRepository.findById(adminId).orElse(null);
        if (admin == null) {
            log.warn("[AdminSecurityService] Admin user not found for ID: {}", adminId);
            return;
        }

        AdminActivityLog log = AdminActivityLog.builder()
                .admin(admin)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .ipAddress(ipAddress)
                .status("SUCCESS")
                .build();

        activityLogRepository.save(log);
    }

    /**
     * Reset user password (admin initiated)
     */
    public void resetUserPassword(Long adminId, Long userId, String newPassword, String ipAddress) {
        log.info("[AdminSecurityService] Admin {} resetting password for user {}", adminId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Encode password
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // Log the activity
        logAdminActivity(adminId, "RESET_PASSWORD", "USER", userId, 
                "Password reset for user: " + user.getEmail(), ipAddress);

        log.info("[AdminSecurityService] Password reset successfully for user ID: {}", userId);
    }

    /**
     * Get security settings
     */
    @Transactional(readOnly = true)
    public AdminSecuritySettingsDTO getSecuritySettings() {
        log.info("[AdminSecurityService] Fetching security settings");
        
        // Placeholder for security settings - would be loaded from config
        return AdminSecuritySettingsDTO.builder()
                .jwtEnabled(true)
                .jwtExpirationMs(86400000L) // 24 hours
                .twoFactorAuthEnabled(false)
                .sessionTimeoutEnabled(true)
                .sessionTimeoutMs(3600000L) // 1 hour
                .maxFailedLoginAttempts(5)
                .passwordMinLength(8)
                .passwordRequireSpecialChar(false)
                .ipWhitelistEnabled(false)
                .build();
    }

    /**
     * Verify JWT integration
     */
    @Transactional(readOnly = true)
    public boolean verifyJWTIntegration() {
        log.info("[AdminSecurityService] Verifying JWT integration");
        // Placeholder - actual JWT verification would be implemented
        return true;
    }

    /**
     * Helper: Convert to DTO
     */
    private AdminActivityLogDTO convertToDTO(AdminActivityLog log) {
        return AdminActivityLogDTO.builder()
                .id(log.getId())
                .adminId(log.getAdmin().getId())
                .adminEmail(log.getAdmin().getEmail())
                .action(log.getAction())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .details(log.getDetails())
                .ipAddress(log.getIpAddress())
                .status(log.getStatus())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
