package com.chanakya.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUserListDTO {

    private Long id;
    private String fullName;
    private String email;
    private Boolean isActive;
    private LocalDateTime lastLogin;
}