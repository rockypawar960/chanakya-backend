package com.chanakya.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminActivityLogDTO {

    private Long id;

    private Long adminId;

    private String adminEmail;

    private String action;

    private String entityType;

    private Long entityId;

    private String details;

    private String ipAddress;

    private String status;

    private LocalDateTime createdAt;
}
