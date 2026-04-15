package com.chanakya.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCareerDetailsDTO {

    private Long id;

    private String name;

    private String description;

    private Integer popularityScore;

    private Boolean isActive;

    private Long totalAssessmentMatches;

    private Double averageUserMatch;

    private Long associatedLearningPathsCount;

    private Long associatedResourcesCount;

    private List learningPaths;

    private List resources;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
