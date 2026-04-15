package com.chanakya.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for enabling/disabling user accounts
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserStatusRequest {
    
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    
    @NotNull(message = "Status cannot be null")
    private Boolean isActive;
}
