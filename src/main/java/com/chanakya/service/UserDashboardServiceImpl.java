package com.chanakya.service;

import com.chanakya.dto.*;
import com.chanakya.entity.Assessment;
import com.chanakya.entity.User;
import com.chanakya.repository.AssessmentRepository;
import com.chanakya.repository.LearningPathRepository;
import com.chanakya.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserDashboardServiceImpl implements UserDashboardService {

    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;
    private final LearningPathRepository learningPathRepository;
    private final RecommendationService recommendationService;
    private final LearningStepService learningStepService;

    @Override
    public UserDashboardDTO getDashboard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDashboardDTO dto = new UserDashboardDTO();

        // 1. Basic Info
        dto.setName(user.getFirstName());

        // 2. Stats (Properly calculated)
        int completedCount = assessmentRepository.countByUserId(userId);
        dto.setAssessmentsCompleted(completedCount);
        dto.setSkillsTracked(completedCount); // Ya jo bhi aapka logic ho
        dto.setLearningPathsEnrolled((int) learningPathRepository.count());
        dto.setTotalLearningHours(completedCount * 5);

        // Profile completion logic (Example: 80%)
        dto.setProfileCompletion(80);

        // 3. Assessment Dependent Data
        assessmentRepository.findTopByUser_IdOrderByCompletedAtDesc(userId)
                .ifPresent(assessment -> {
                    dto.setScore(assessment.getScore());
                    dto.setBucketScores(assessment.getBucketScores());

                    // FETCH (Don't Generate here to avoid locking error)
                    dto.setRecommendations(recommendationService.getRecommendationsByAssessmentId(assessment.getId()));

                    // Roadmap
                    dto.setRoadmap(learningStepService.getStepsByPath(1L));
                });

        // 4. Resources
        dto.setResources(new ArrayList<>());

        return dto;
    }
    // ================= HELPER METHODS =================

}