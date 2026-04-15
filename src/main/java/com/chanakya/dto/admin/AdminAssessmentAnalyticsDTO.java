package com.chanakya.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO for assessment analytics and statistics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAssessmentAnalyticsDTO {
    
    // Basic statistics
    private Long totalAssessments;
    private Long activeAssessments;
    private Long inactiveAssessments;
    private Double averageScore;
    
    // Score distribution
    private Long assessmentsBelowAverage;  // score < average
    private Long assessmentsAboveAverage;  // score > average
    private Long assessmentsAtAverage;     // score = average
    
    // Bucket statistics
    private Map<String, Double> averageBucketScores;
    
    // Time-based analytics
    private Long assessmentsThisWeek;
    private Long assessmentsThisMonth;
    private Long assessmentsThisYear;
    
    // Top scores
    private List<AdminAssessmentDTO> topAssessments;
    
    // Score range distribution
    private Long scoreRange0to20;
    private Long scoreRange20to40;
    private Long scoreRange40to60;
    private Long scoreRange60to80;
    private Long scoreRange80to100;
}
