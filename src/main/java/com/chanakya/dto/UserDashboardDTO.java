package com.chanakya.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserDashboardDTO {
    private String name;
    private int profileCompletion;
    private int totalLearningHours;
    private int assessmentsCompleted;
    private int learningPathsEnrolled;

    private int skillsTracked;

    private Integer score;
    private Map<String, Integer> bucketScores;

    private List<RecommendationDTO> recommendations;
    private List<LearningStepDTO> roadmap;
    private List<ResourceDTO> resources;
}