package com.chanakya.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for assessment summary in list view
 * Used in paginated assessment listing
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAssessmentDTO {
    private Long id;
    private Long userId;
    private String userEmail;
    private String userFullName;
    private Integer totalScore;
    private LocalDateTime completedAt;
    private Boolean isActive;
    
    @JsonProperty("bucketScores")
    // AdminAssessmentDetailsDTO.java
    private Map<String, Integer> bucketScores;  // Integer kar do, Object ki jagah
}
