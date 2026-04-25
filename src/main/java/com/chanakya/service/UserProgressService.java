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

        if (status.equalsIgnoreCase("in_progress") && progress.getStartedAt() == null) {
            progress.setStartedAt(LocalDateTime.now());
        }

        if (status.equals("completed")) {
            progress.setCompletedAt(LocalDateTime.now());
        }

        progress.setStatus(status);

        progressRepo.save(progress);
    }
    public double calculateProgress(Long userId, Long pathId) {
        // 1. Total steps nikalo
        long totalSteps = stepRepo.countStepsByPathId(pathId);
        if (totalSteps == 0) return 0;

        // 2. Woh steps gino jo ya toh start ho chuke hain ya khatam
        long activeSteps = progressRepo.countStartedOrCompletedSteps(userId, pathId);

        // 3. Percentage calculate karo
        double percentage = ((double) activeSteps / totalSteps) * 100;

        // 4. Safetly check: Agar koi step active hai toh kam se kam 1% return karo
        if (percentage == 0 && activeSteps > 0) {
            return 1.0;
        }

        return percentage;
    }


    public String getPathStatus(Long userId, Long pathId) {

        double progress = calculateProgress(userId, pathId);

        if (progress == 0) return "NOT_STARTED";
        if (progress < 100) return "ENROLLED";
        return "COMPLETED";
    }
}