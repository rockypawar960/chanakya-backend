package com.chanakya.service;

import com.chanakya.entity.UserProgress;
import com.chanakya.repository.LearningStepRepository;
import com.chanakya.repository.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserProgressService {

    private final UserProgressRepository progressRepo;

    private final LearningStepRepository stepRepo;

    public void updateStepStatus(Long userId, Long stepId, String status) {

        UserProgress progress = progressRepo
                .findByUserIdAndLearningStepId(userId, stepId)
                .orElse(
                        UserProgress.builder()
                                .userId(userId)
                                .learningStepId(stepId)
                                .status("not_started")
                                .build()
                );

        // ✅ Start time set only once
        if (status.equals("in_progress") && progress.getStartedAt() == null) {
            progress.setStartedAt(LocalDateTime.now());
        }

        // ✅ Complete time set
        if (status.equals("completed")) {
            progress.setCompletedAt(LocalDateTime.now());
        }

        progress.setStatus(status);

        progressRepo.save(progress);
    }

    public double calculateProgress(Long userId, Long pathId) {

        long totalSteps = stepRepo.countStepsByPathId(pathId);

        long completedSteps = progressRepo
                .countCompletedStepsByUserAndPath(userId, pathId);

        if (totalSteps == 0) return 0;

        return ((double) completedSteps / totalSteps) * 100;
    }
}