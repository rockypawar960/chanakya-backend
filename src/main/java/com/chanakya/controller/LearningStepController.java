package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.LearningStepDTO;
import com.chanakya.service.LearningStepService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/learning-steps")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Learning Steps", description = "Learning steps endpoints")
public class LearningStepController {

    private final LearningStepService stepService;

    @GetMapping("/path/{pathId}")
    @Operation(summary = "Get steps by learning path",
            description = "Fetch all steps for a specific learning path")
    public ResponseEntity<ApiResponse<List<LearningStepDTO>>> getStepsByPath(
            @PathVariable Long pathId) {

        log.info("Fetching steps for path id: {}", pathId);

        List<LearningStepDTO> steps = stepService.getStepsByPath(pathId);

        return ResponseEntity.ok(
                ApiResponse.<List<LearningStepDTO>>builder()
                        .success(true)
                        .message(steps.isEmpty() ? "No steps found" : "Steps retrieved successfully")
                        .data(steps)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}