package com.chanakya.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRecommendationConfigDTO {

    private Long id;

    @NotBlank(message = "Bucket name is required")
    private String bucketName;

    @Min(value = 0, message = "Weight must be >= 0")
    @Max(value = 100, message = "Weight must be <= 100")
    private Double weight;

    @Min(value = 0, message = "Threshold score must be >= 0")
    @Max(value = 100, message = "Threshold score must be <= 100")
    private Double thresholdScore;

    private Boolean isActive;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
