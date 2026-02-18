package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.LearningPathDTO;
import com.chanakya.service.LearningPathService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/learning-paths")
@RequiredArgsConstructor
@Tag(name = "Learning Path", description = "Learning path endpoints")
@Slf4j
public class LearningPathController {

    private final LearningPathService learningPathService;

    @GetMapping("/career/{careerId}")
    @Operation(summary = "Get learning paths for career", description = "Get all learning paths for a specific career")
    public ResponseEntity<ApiResponse<List<LearningPathDTO>>> getLearningPathsByCareer(
            @PathVariable Long careerId) {

        log.info("Fetching learning paths for career id: {}", careerId);
        List<LearningPathDTO> paths = learningPathService.getLearningPathsByCareer(careerId);

        // 🔥 FIX: Wrap in ApiResponse with proper generic type
        return ResponseEntity.ok(
                ApiResponse.<List<LearningPathDTO>>builder()
                        .success(true)
                        .message(paths.isEmpty() ? "No learning paths found" : "Learning paths retrieved successfully")
                        .data(paths)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}