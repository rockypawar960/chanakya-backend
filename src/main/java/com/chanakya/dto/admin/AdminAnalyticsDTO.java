package com.chanakya.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAnalyticsDTO {

    // Career Analytics
    private List<CareerInterestDTO> topCareers;
    private Map<String, Long> careerDistribution;
    private Long totalCareersCount;

    // User Analytics
    private Long totalUsersCount;
    private Long activeUsersCount;
    private Long inactiveUsersCount;

    // Assessment Analytics
    private Long totalAssessmentsCount;
    private Double averageAssessmentScore;
    private Long totalCompletedAssessments;

    // Drop-off Analytics
    private Double userCompletionRate;
    private Double assessmentCompletionRate;
    private Long dropOffCount;

    // Skills/Bucket Analytics
    private Map<String, Long> skillDemand;
    private Map<String, Double> bucketAverageScores;

    // Learning Path Analytics
    private Long totalLearningPathsCount;
    private Long enrolledLearningPathsCount;

    // Resource Analytics
    private Long totalResourcesCount;
    private Map<String, Long> resourceTypeDistribution;
}
