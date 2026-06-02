package com.chanakya.service;

import com.chanakya.dto.ProgressDataDTO;

public interface ProgressTrackerService {
    ProgressDataDTO getProgressData(Long userId);
}