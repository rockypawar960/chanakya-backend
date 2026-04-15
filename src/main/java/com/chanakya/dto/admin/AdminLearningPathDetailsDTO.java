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
public class AdminLearningPathDetailsDTO {

    private Long id;

    private Long careerId;

    private String pathName;

    private String description;

    private Integer durationMonths;

    private String careerName;

    private List steps;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
