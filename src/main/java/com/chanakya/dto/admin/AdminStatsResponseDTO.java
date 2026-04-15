package com.chanakya.dto.admin;

import lombok.*;
import java.util.*;

/**
 * Wrapper DTO for dashboard statistics response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStatsResponseDTO {
    private AdminDashboardDTO dashboard;
    private String timestamp;
    private String message;
}
