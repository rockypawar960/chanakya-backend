package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.LearningPathDTO;
import com.chanakya.security.AuthUtil;
import com.chanakya.service.LearningPathService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/learning-paths")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Learning Path", description = "Learning path endpoints")
public class LearningPathController {

    private final LearningPathService pathService;
    private final AuthUtil authUtil;

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<LearningPathDTO>> getCurrentPath() {

        log.info("CURRENT PATH API HIT");

        long userId= authUtil.getCurrentUserId();
        LearningPathDTO dto = pathService.getCurrentActivePath(userId);

        return ResponseEntity.ok(
                ApiResponse.<LearningPathDTO>builder()
                        .success(true)
                        .message("Current learning path fetched successfully")
                        .data(dto)
                        .status(200)
                        .build()
        );
    }

    @PostMapping("/{pathId}/set-current")
    public ResponseEntity<ApiResponse<String>> setCurrentPath(
            @PathVariable Long pathId) {

        Long userId = authUtil.getCurrentUserId();

        pathService.setCurrentPath(userId, pathId);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Current path updated")
                        .data("Success")
                        .status(200)
                        .build()
        );
    }

    // ─── EXISTING ────────────────────────────────────────────────────────────
    @GetMapping("/path/{pathId}")
    @Operation(summary = "Get learning path by ID")
    public ResponseEntity<ApiResponse<LearningPathDTO>> getFullPath(
            @PathVariable Long pathId) {

        LearningPathDTO dto = pathService.getPathWithSteps(pathId);
        return ResponseEntity.ok(ApiResponse.<LearningPathDTO>builder()
                .success(true)
                .message("Learning path fetched successfully")
                .data(dto)
                .status(200)
                .build());
    }

    // ─── EXISTING ────────────────────────────────────────────────────────────
    @GetMapping("/careerId/{carrierId}")
    @Operation(summary = "Get learning path by career ID")
    public ResponseEntity<ApiResponse<LearningPathDTO>> getFullPathByCareerId(
            @PathVariable Long carrierId) {

        log.info("===== CAREER PATH API HIT : {} =====", carrierId);

        LearningPathDTO dto = pathService.getPathWithStepsByCareerId(carrierId);
        return ResponseEntity.ok(ApiResponse.<LearningPathDTO>builder()
                .success(true)
                .message("Learning path fetched successfully")
                .data(dto)
                .status(200)
                .build());
    }

    // ─── NEW: User clicks a recommendation card on UI ─────────────────────
    // Frontend sends ONLY userId + careerId — no prompt, no body needed
    @PostMapping("/generate")
    @Operation(summary = "Generate personalized AI roadmap from recommendation")
    public ResponseEntity<ApiResponse<LearningPathDTO>> generateLearningPath(
            @RequestParam Long careerId) {

         Long userId= authUtil.getCurrentUserId();

        log.info("Generate roadmap request → userId={} careerId={}", userId, careerId);

        LearningPathDTO dto = pathService.generateAndSave(userId, careerId);

        return ResponseEntity.ok(ApiResponse.<LearningPathDTO>builder()
                .success(true)
                .message("AI learning path generated successfully")
                .data(dto)
                .status(200)
                .build());
    }
}