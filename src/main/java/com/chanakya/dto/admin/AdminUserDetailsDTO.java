package com.chanakya.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Detailed DTO for user information including assessment history
 * Used in GET /api/admin/users/{userId} endpoint
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserDetailsDTO {
    
    // Basic user info
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String classOrYear;
    private String stream;
    private String interests;
    private String strengths;
    private String challenges;
    private Boolean isActive;
    
    // Role information
    private Set roleNames;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    
    // Assessment history
    private List assessmentHistory;
    private Integer totalAssessments;
    private Integer averageAssessmentScore;
}
