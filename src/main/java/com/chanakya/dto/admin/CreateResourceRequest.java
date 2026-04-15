package com.chanakya.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateResourceRequest {

    @NotNull(message = "Career ID is required")
    private Long careerId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Resource type is required (COURSE, ARTICLE, BOOK, VIDEO, PLATFORM)")
    private String resourceType;

    @NotBlank(message = "URL is required")
    private String url;

    private String provider;

    private String difficulty;

    private String estimatedDuration;

    private Boolean isActive = true;
}
