package com.chanakya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningStepDTO {

    private Long id;

    private Long learningPathId;   // FK reference

    private String level;          // beginner / intermediate / advanced

    private String stepName;
    private String description;

    private String videoLink;
    private String task;

    private Integer stepOrder;
}