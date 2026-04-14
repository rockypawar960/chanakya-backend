package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.service.UserProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
public class UserProgressController {

    private final UserProgressService progressService;

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<String>> updateProgress(
            @RequestParam Long userId,
            @RequestParam Long stepId,
            @RequestParam String status) {

        progressService.updateStepStatus(userId, stepId, status);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Progress updated successfully")
                        .data(status.toUpperCase())
                        .status(200)
                        .build()
        );
    }

    @GetMapping("/percentage")
    public ResponseEntity<ApiResponse<Double>> getProgress(
            @RequestParam Long userId,
            @RequestParam Long pathId) {

        double progress = progressService.calculateProgress(userId, pathId);

        return ResponseEntity.ok(
                ApiResponse.<Double>builder()
                        .success(true)
                        .message("Progress fetched")
                        .data(progress)
                        .status(200)
                        .build()
        );
    }
}