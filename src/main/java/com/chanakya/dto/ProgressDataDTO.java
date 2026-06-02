package com.chanakya.dto;

import lombok.Data;

@Data
public class ProgressDataDTO {

    private int totalAssessments;
    private int completedAssessments;
    private int enrolledLearningPaths;
    private int completedLearningPaths;
    private int totalResourcesViewed;
    private int bookmarkedResources;
    private int currentStreak;
    private int totalLearningHours;
    private String lastActivityDate;  // ISO-8601: "2025-06-01"
}
