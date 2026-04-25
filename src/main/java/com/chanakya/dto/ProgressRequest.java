package com.chanakya.dto;

import lombok.Data;

@Data
public class ProgressRequest {
    private Long stepId;
    private String status; // IN_PROGRESS / COMPLETED
}
