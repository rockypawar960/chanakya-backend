package com.chanakya.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLearningPathRequest {

    @NotNull(message = "Career ID is required")
    private Long careerId;

    @NotBlank(message = "Path name is required")
    private String pathName;

    private String description;

    @Min(value = 1, message = "Duration must be at least 1 month")
    private Integer durationMonths;
}
