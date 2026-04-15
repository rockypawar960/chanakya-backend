package com.chanakya.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCareerDTO {

    private Long id;

    @NotBlank(message = "Career name is required")
    private String name;

    private String description;

    @Min(value = 0, message = "Popularity score cannot be negative")
    private Integer popularityScore;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
