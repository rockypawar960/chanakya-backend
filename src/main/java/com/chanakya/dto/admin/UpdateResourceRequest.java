package com.chanakya.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateResourceRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private String resourceType;

    @NotBlank(message = "URL is required")
    private String url;

    private String provider;

    private String difficulty;

    private String estimatedDuration;

    private Boolean isActive;
}
