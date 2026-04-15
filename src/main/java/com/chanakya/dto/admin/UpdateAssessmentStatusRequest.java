package com.chanakya.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating assessment status (enable/disable)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAssessmentStatusRequest {
    
    @NotNull(message = "Assessment ID cannot be null")
    private Long assessmentId;
    
    @NotNull(message = "isActive status cannot be null")
    private Boolean isActive;
}
