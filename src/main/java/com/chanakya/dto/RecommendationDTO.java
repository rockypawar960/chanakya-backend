package com.chanakya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDTO {
    private Long id;
    private Long userId;
    private Long careerId;
    private String careerName;
    private Double matchScore;
    private String reasoning;
    private Boolean isActive;
}