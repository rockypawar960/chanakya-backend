package com.chanakya.service.admin;

import com.chanakya.dto.admin.*;
import com.chanakya.entity.Assessment;
import com.chanakya.entity.Role;
import com.chanakya.entity.User;
import com.chanakya.repository.AssessmentRepository;
import com.chanakya.repository.RoleRepository;
import com.chanakya.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserService {

    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;
    private final RoleRepository roleRepository;

    /**
     * Get paginated list of all users
     */
    @Transactional(readOnly = true)
    public PaginatedResponse<AdminUserDTO> getAllUsers(Pageable pageable) {  // ✅ Added generic
        log.info("Fetching paginated users with pageable: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        try {
            Page<User> userPage = userRepository.findAll(pageable);  // ✅ Added <User>

            List<AdminUserDTO> userDTOs = userPage.getContent().stream()  // ✅ Added <AdminUserDTO>
                    .map(this::convertToAdminUserDTO)
                    .collect(Collectors.toList());

            return PaginatedResponse.<AdminUserDTO>builder()  // ✅ Type witness
                    .content(userDTOs)
                    .pageNumber(userPage.getNumber())
                    .pageSize(userPage.getSize())
                    .totalElements(userPage.getTotalElements())
                    .totalPages(userPage.getTotalPages())
                    .isLast(userPage.isLast())
                    .isEmpty(userPage.isEmpty())
                    .build();

        } catch (Exception e) {
            log.error("Error fetching paginated users", e);
            return PaginatedResponse.<AdminUserDTO>builder()  // ✅ Type witness
                    .content(new ArrayList<>())
                    .pageNumber(0)
                    .pageSize(0)
                    .totalElements(0)
                    .totalPages(0)
                    .isLast(true)
                    .isEmpty(true)
                    .build();
        }
    }

    /**
     * Get detailed information for a specific user including assessment history
     */
    @Transactional(readOnly = true)
    public AdminUserDetailsDTO getUserDetailsWithAssessmentHistory(Long userId) {
        log.info("Fetching user details with assessment history for userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Fetch assessment history for this user
        List<Assessment> assessments = assessmentRepository.findByUserIdAndIsActiveTrueOrderByCompletedAtDesc(userId);  // ✅ Added <Assessment>

        List<AdminAssessmentHistoryDTO> assessmentHistoryDTOs = assessments.stream()  // ✅ Added generic
                .map(this::convertToAssessmentHistoryDTO)
                .collect(Collectors.toList());

        Integer averageScore = assessments.isEmpty() ? 0 :
                (int) assessments.stream()
                        .mapToInt(Assessment::getTotalScore)
                        .average()
                        .orElse(0.0);

        return AdminUserDetailsDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .classOrYear(user.getClassOrYear())
                .stream(user.getStream())
                .interests(user.getInterests())
                .strengths(user.getStrengths())
                .challenges(user.getChallenges())
                .isActive(user.getIsActive())
                .roleNames(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .assessmentHistory(assessmentHistoryDTOs)
                .totalAssessments(assessments.size())
                .averageAssessmentScore(averageScore)
                .build();
    }

    /**
     * Enable or disable a user account
     */
    @Transactional
    public AdminUserDTO updateUserStatus(Long userId, Boolean isActive) {
        log.info("Updating user status for userId: {}, isActive: {}", userId, isActive);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        user.setIsActive(isActive);
        User updatedUser = userRepository.save(user);

        log.info("User status updated successfully. UserId: {}, IsActive: {}", userId, isActive);
        return convertToAdminUserDTO(updatedUser);
    }

    /**
     * Update user role (assign or update role)
     */
    @Transactional
    public AdminUserDTO updateUserRole(Long userId, String roleName) {
        log.info("Updating user role for userId: {}, roleName: {}", userId, roleName);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with name: " + roleName));

        // Replace existing roles with new role
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        User updatedUser = userRepository.save(user);

        log.info("User role updated successfully. UserId: {}, Role: {}", userId, roleName);
        return convertToAdminUserDTO(updatedUser);
    }

    /**
     * Delete a user account
     */
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Deleting user with userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        userRepository.delete(user);
        log.info("User deleted successfully. UserId: {}", userId);
    }

    /**
     * Convert User entity to AdminUserDTO
     */
    private AdminUserDTO convertToAdminUserDTO(User user) {
        // Get latest assessment score if exists
        Optional<Assessment> latestAssessment = assessmentRepository  // ✅ Added <Assessment>
                .findTopByUserIdAndIsActiveTrueOrderByCompletedAtDesc(user.getId());

        // Count total assessments
        List<Assessment> assessments = assessmentRepository  // ✅ Added <Assessment>
                .findByUserIdAndIsActiveTrueOrderByCompletedAtDesc(user.getId());

        // Get role names safely
        Set<String> roleNames = user.getRoles() != null ?
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()) :
                new HashSet<>();

        return AdminUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .classOrYear(user.getClassOrYear())
                .stream(user.getStream())
                .interests(user.getInterests())
                .strengths(user.getStrengths())
                .challenges(user.getChallenges())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .roleNames(roleNames)
                .assessmentCount((long) assessments.size())
                .totalAssessmentScore(latestAssessment.map(Assessment::getTotalScore).orElse(null))
                .build();
    }

    /**
     * Convert Assessment entity to AdminAssessmentHistoryDTO
     */
    private AdminAssessmentHistoryDTO convertToAssessmentHistoryDTO(Assessment assessment) {
        return AdminAssessmentHistoryDTO.builder()
                .assessmentId(assessment.getId())
                .totalScore(assessment.getTotalScore())
                .completedAt(assessment.getCompletedAt())
                .bucketScores(assessment.getBucketScores() != null ?
                        convertBucketScores(assessment.getBucketScores()) :
                        new HashMap<>())
                .isActive(assessment.getIsActive())
                .build();
    }

    /**
     * Helper method to convert bucket scores
     */
    private Map<String, Integer> convertBucketScores(Map<String, Integer> bucketScores) {
        if (bucketScores == null) {
            return new HashMap<>();
        }
        return bucketScores;
    }
}