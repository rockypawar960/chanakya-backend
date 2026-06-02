package com.chanakya.service;

import com.chanakya.dto.LearningPathDTO;
import com.chanakya.dto.LearningStepDTO;
import com.chanakya.entity.*;
import com.chanakya.repository.LearningPathRepository;
import com.chanakya.repository.UserLearningPathRepository;
import com.chanakya.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.chanakya.entity.UserLearningPath.LearningPathStatus.ACTIVE;

@Service
@RequiredArgsConstructor
@Slf4j
public class LearningPathService {


    private final LearningPathRepository pathRepo;
    private final UserLearningPathRepository userLearningPathRepository;
    private final UserRepository userRepository;
    private final GeminiAgentService geminiAgentService;
    private final PromptBuilderService promptBuilderService;  // ← builds prompt from DB

    @Transactional(readOnly = true)
    public LearningPathDTO getCurrentActivePath(Long userId) {


        UserLearningPath userPath =
                userLearningPathRepository
                        .findByUserIdAndIsCurrentTrue(userId)
                        .orElseThrow(() ->
                                new RuntimeException("No active learning path found"));

        return getPathWithSteps(
                userPath.getLearningPath().getId()
        );
    }


    // ─── EXISTING: get by pathId ─────────────────────────────────────────────
    public LearningPathDTO getPathWithSteps(Long pathId) {
        LearningPath path = pathRepo.findById(pathId)
                .orElseThrow(() -> new RuntimeException("Path not found"));
        return convertToDTO(path);
    }

    // ─── EXISTING: get by careerId ───────────────────────────────────────────
    public LearningPathDTO getPathWithStepsByCareerId(Long carrierId) {
        List<LearningPath> paths = pathRepo.findByCareerId(carrierId);
        LearningPath path = paths.get(0);
        return convertToDTO(path);
    }

    //  ─── Set Current active path ───────────────────────────

    @Transactional
    public void setCurrentPath(Long userId, Long pathId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LearningPath path = pathRepo.findById(pathId)
                .orElseThrow(() -> new RuntimeException("Path not found"));

        // Old current path remove
        userLearningPathRepository.clearCurrentPath(userId);

        // Check already exists
        UserLearningPath userPath =
                userLearningPathRepository
                        .findByUserIdAndLearningPathId(userId, pathId)
                        .orElse(null);

        if (userPath == null) {
            userPath = UserLearningPath.builder()
                    .user(user)
                    .learningPath(path)
                    .status(ACTIVE)
                    .progressPercentage(0)
                    .isCurrent(true)
                    .build();
        } else {
            userPath.setCurrent(true);
        }

        userLearningPathRepository.save(userPath);
        }

    // ─── NEW: Frontend sends only userId + careerId ───────────────────────────
    // Backend builds prompt from recommendation → Gemini generates → saves to DB
    @Transactional
    public LearningPathDTO generateAndSave(Long userId, Long careerId) {

        // ==========================
        // STEP 1: Check existing path
        // ==========================
        List<LearningPath> existingPaths = pathRepo.findByCareerId(careerId);

        LearningPath path;

        if (!existingPaths.isEmpty()) {

            log.info("Existing learning path found for careerId={}", careerId);

            path = existingPaths.get(0);

        } else {

            log.info("No path found. Generating new path for careerId={}", careerId);

            // Build prompt
            String prompt = promptBuilderService.buildPrompt(userId, careerId);

            // Gemini Generate
            LearningPathDTO aiDto =
                    geminiAgentService.generateLearningPath(careerId, prompt);

            // Create Learning Path
            LearningPath learningPath = LearningPath.builder()
                    .careerId(careerId)
                    .pathName(aiDto.getPathName())
                    .description(aiDto.getDescription())
                    .durationMonths(aiDto.getDurationMonths())
                    .isActive(true)
                    .build();

            // Create Steps
            List<LearningStep> steps = aiDto.getSteps().stream()
                    .map(stepDto -> LearningStep.builder()
                            .level(stepDto.getLevel())
                            .stepName(stepDto.getStepName())
                            .description(stepDto.getDescription())
                            .videoLink(stepDto.getVideoLink())
                            .task(stepDto.getTask())
                            .stepOrder(stepDto.getStepOrder())
                            .learningPath(learningPath)
                            .build())
                    .toList();

            learningPath.setSteps(steps);

            // Save Path
            path = pathRepo.save(learningPath);

            log.info("Generated and saved new path id={}", path.getId());
        }

        // ==========================
        // STEP 2: Set as Current Path
        // ==========================
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userLearningPathRepository.clearCurrentPath(userId);

        UserLearningPath userPath = UserLearningPath.builder()
                .user(user)
                .learningPath(path)
                .status(ACTIVE)
                .progressPercentage(0)
                .isCurrent(true)
                .build();

        userLearningPathRepository.save(userPath);

        log.info("Current path updated. userId={} pathId={}",
                userId, path.getId());

        return convertToDTO(path);
    }
    // ─── DTO converter ───────────────────────────────────────────────────────
    private LearningPathDTO convertToDTO(LearningPath path) {
        return LearningPathDTO.builder()
                .id(path.getId())
                .careerId(path.getCareerId())
                .pathName(path.getPathName())
                .description(path.getDescription())
                .durationMonths(path.getDurationMonths())
                .steps(path.getSteps().stream().map(step ->
                        LearningStepDTO.builder()
                                .id(step.getId())
                                .learningPathId(path.getId())
                                .level(step.getLevel())
                                .stepName(step.getStepName())
                                .description(step.getDescription())
                                .videoLink(step.getVideoLink())
                                .task(step.getTask())
                                .stepOrder(step.getStepOrder())
                                .build()
                ).toList())
                .build();
    }
}