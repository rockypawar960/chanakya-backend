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
public class UpdateFeedbackResponseRequest {

    @NotBlank(message = "Admin response is required")
    private String adminResponse;

    @NotBlank(message = "Status is required (OPEN, IN_PROGRESS, RESOLVED, CLOSED)")
    private String status;
}
