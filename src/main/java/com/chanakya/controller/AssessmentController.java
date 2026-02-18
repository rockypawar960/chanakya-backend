package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.AssessmentRequest;
import com.chanakya.dto.QuestionDTO;
import com.chanakya.entity.Assessment;
import com.chanakya.service.AssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments")
@RequiredArgsConstructor
@Tag(name = "Assessment", description = "Assessment related endpoints")
public class AssessmentController {

    private final AssessmentService assessmentService;

    @GetMapping("/questions")
    @Operation(summary = "Get all assessment questions", description = "Fetch all active questions for the assessment")
    public ResponseEntity<ApiResponse<List<QuestionDTO>>> getAllQuestions() {
        List<QuestionDTO> questions = assessmentService.getAllQuestions();

        return ResponseEntity.ok(
                ApiResponse.<List<QuestionDTO>>builder()
                        .success(true)
                        .message(questions.isEmpty() ? "No questions found" : "Questions retrieved successfully")
                        .data(questions)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/submit")
    @Operation(summary = "Submit assessment", description = "Submit completed assessment and get recommendations")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ApiResponse<Assessment>> submitAssessment(
            @Valid @RequestBody AssessmentRequest request) {

        Long userId = extractUserIdFromAuthentication();
        Assessment assessment = assessmentService.submitAssessment(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<Assessment>builder()
                        .success(true)
                        .message("Assessment submitted successfully")
                        .data(assessment)
                        .status(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @GetMapping("/my/latest")
    @Operation(summary = "Get latest assessment", description = "Get the most recent assessment for the current user")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ApiResponse<Assessment>> getLatestAssessment() {
        Long userId = extractUserIdFromAuthentication();
        Assessment assessment = assessmentService.getLatestAssessmentByUserId(userId);

        if (assessment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Assessment>builder()
                            .success(false)
                            .message("No assessment found")
                            .data(null)
                            .status(HttpStatus.NOT_FOUND.value())
                            .build()
            );
        }

        return ResponseEntity.ok(
                ApiResponse.<Assessment>builder()
                        .success(true)
                        .message("Assessment retrieved successfully")
                        .data(assessment)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/my/all")
    @Operation(summary = "Get all assessments", description = "Get all assessments for the current user")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ApiResponse<List<Assessment>>> getAllAssessments() {
        Long userId = extractUserIdFromAuthentication();
        List<Assessment> assessments = assessmentService.getAllAssessmentsByUserId(userId);

        return ResponseEntity.ok(
                ApiResponse.<List<Assessment>>builder()
                        .success(true)
                        .message(assessments.isEmpty() ? "No assessments found" : "Assessments retrieved successfully")
                        .data(assessments)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    private Long extractUserIdFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // TODO: Implement proper user ID extraction from database
        return 1L; // Temporary dummy ID
    }
}