package com.chanakya.service;

import com.chanakya.dto.UserDashboardDTO;

public interface UserDashboardService {
    UserDashboardDTO getDashboard(Long userId);
}
