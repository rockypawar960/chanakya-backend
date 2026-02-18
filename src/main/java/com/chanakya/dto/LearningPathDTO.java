package com.chanakya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningPathDTO {

    private Long id;
    private Long careerId;
    private String pathName;
    private String description;
    private String skills;
    private String resources;
    private Integer sequenceNumber;
    private Integer durationMonths;
}
