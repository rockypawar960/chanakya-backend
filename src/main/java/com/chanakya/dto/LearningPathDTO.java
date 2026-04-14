package com.chanakya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningPathDTO {

    private Long id;
    private Long careerId;

    private String pathName;
    private String description;

    private Integer sequenceNumber;
    private Integer durationMonths;

    // 🔥 important
    private List<LearningStepDTO> steps;
}
