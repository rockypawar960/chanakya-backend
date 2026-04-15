package com.chanakya.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminSecuritySettingsDTO {

    private Boolean jwtEnabled;

    private Long jwtExpirationMs;

    private String jwtSecret;

    private Boolean twoFactorAuthEnabled;

    private Boolean sessionTimeoutEnabled;

    private Long sessionTimeoutMs;

    private Integer maxFailedLoginAttempts;

    private Integer passwordMinLength;

    private Boolean passwordRequireSpecialChar;

    private Boolean ipWhitelistEnabled;

    private String ipWhitelist;
}
