package com.chanakya.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateLearningPathRequest {

    @NotBlank(message = "Path name is required")
    private String pathName;

    private String description;

    @Min(value = 1, message = "Duration must be at least 1 month")
    private Integer durationMonths;
}
