package com.chanakya.dto.admin;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserDTO {

    private Long id;
    private String email;
    private String fullName;

    private String classOrYear;
    private String stream;

    private String interests;
    private String strengths;
    private String challenges;

    private Set<String> roles;

    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}