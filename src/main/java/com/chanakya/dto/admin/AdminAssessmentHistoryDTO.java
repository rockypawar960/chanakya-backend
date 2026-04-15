package com.chanakya.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for assessment history in user details view
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAssessmentHistoryDTO {
    
    private Long assessmentId;
    private Integer totalScore;
    private LocalDateTime completedAt;
    private Map bucketScores;
    private Boolean isActive;
}
