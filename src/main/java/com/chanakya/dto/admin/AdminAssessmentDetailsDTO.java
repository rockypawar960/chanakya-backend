package com.chanakya.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for detailed assessment information
 * Includes raw responses and detailed bucket scores
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAssessmentDetailsDTO {
    private Long id;
    private Long userId;
    private String userEmail;
    private String userFullName;
    private Integer totalScore;
    private LocalDateTime completedAt;
    private Boolean isActive;
    
    @JsonProperty("bucketScores")
    private Map<String, Integer> bucketScores;
    
    @JsonProperty("rawResponses")
    private Map<String, Object> rawResponses;
}
