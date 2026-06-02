package com.chanakya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {

    private Long id;
    private Long careerId;
    private String careerName;

    private String title;
    private String description;

    // PLAYLIST, VIDEO, COURSE, ARTICLE, PLATFORM, ROADMAP
    private String resourceType;

    private String url;
    private String provider;          // "YouTube", "Coursera", "NPTEL", "LeetCode"

    // BEGINNER, INTERMEDIATE, ADVANCED
    private String difficulty;

    private String estimatedDuration; // "12 hours", "4 weeks"
    private String language;          // "Hindi", "English", "Both"
    private String skill;             // "Java", "DSA", "Spring Boot"

    private Boolean isActive;
}