package com.chanakya.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResourceDTO {

    private Long id;

    private Long careerId;

    private String careerName;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Resource type is required")
    private String resourceType;

    @NotBlank(message = "URL is required")
    private String url;

    private String provider;

    private String difficulty;

    private String estimatedDuration;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
