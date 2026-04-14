package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.LearningPathDTO;
import com.chanakya.service.LearningPathService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/learning-paths")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Learning Path", description = "Learning path endpoints")
public class LearningPathController {

    private final LearningPathService pathService;

    @GetMapping("/{pathId}/full")
    public ResponseEntity<ApiResponse<LearningPathDTO>> getFullPath(
            @PathVariable Long pathId) {

        LearningPathDTO dto = pathService.getPathWithSteps(pathId);

        return ResponseEntity.ok(
                ApiResponse.<LearningPathDTO>builder()
                        .success(true)
                        .message("Learning path with steps fetched successfully")
                        .data(dto)
                        .status(200)
                        .build()
        );
    }
}