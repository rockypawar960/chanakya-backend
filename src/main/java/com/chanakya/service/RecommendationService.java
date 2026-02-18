package com.chanakya.service;

import com.chanakya.dto.RecommendationDTO;
import com.chanakya.entity.Assessment;
import com.chanakya.entity.Career;
import com.chanakya.entity.Recommendation;
import com.chanakya.entity.User;
import com.chanakya.exception.ResourceNotFoundException;
import com.chanakya.repository.CareerRepository;
import com.chanakya.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecommendationService {

    private final CareerRepository careerRepository;
    private final RecommendationRepository recommendationRepository;

    /**
     * Generate recommendations based on assessment
     */
    public List<RecommendationDTO> generateRecommendations(Assessment assessment) {
        log.info("Generating recommendations for assessment id: {}", assessment.getId());

        try {
            // Get top careers based on popularity
            List<Career> topCareers = careerRepository.findTop5ByIsActiveTrueOrderByPopularityScoreDesc();

            if (topCareers.isEmpty()) {
                log.warn("No careers found in database");
                return List.of();
            }

            // Create recommendations for each career
            List<Recommendation> recommendations = topCareers.stream()
                    .map(career -> createRecommendation(assessment, career))
                    .collect(Collectors.toList());

            // Save all recommendations
            List<Recommendation> savedRecommendations = recommendationRepository.saveAll(recommendations);
            log.info("Generated {} recommendations for assessment id: {}", savedRecommendations.size(), assessment.getId());

            // Convert to DTOs and return
            return savedRecommendations.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error generating recommendations: {}", e.getMessage(), e);
            return List.of();
        }
    }

    /**
     * Get recommendations by user ID
     */
    public List<RecommendationDTO> getRecommendationsByUserId(Long userId) {
        log.info("Fetching recommendations for user id: {}", userId);

        return recommendationRepository.findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get recommendations by assessment ID
     */
    public List<RecommendationDTO> getRecommendationsByAssessmentId(Long assessmentId) {
        log.info("Fetching recommendations for assessment id: {}", assessmentId);

        return recommendationRepository.findByAssessmentIdAndIsActiveTrue(assessmentId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create recommendation entity
     */
    private Recommendation createRecommendation(Assessment assessment, Career career) {
        return Recommendation.builder()
                .user(assessment.getUser())
                .assessment(assessment)
                .career(career)
                .matchScore(calculateMatchScore(assessment, career))
                .reasoning("Based on your assessment responses, this career matches your profile.")
                .isActive(true)
                .build();
    }

    /**
     * Calculate match score between assessment and career
     */
    private Double calculateMatchScore(Assessment assessment, Career career) {
        // TODO: Implement actual matching algorithm based on assessment answers
        // This is a placeholder - generate random score between 70-95
        return 70.0 + (Math.random() * 25);
    }

    /**
     * Map Recommendation entity to DTO
     */
    private RecommendationDTO mapToDTO(Recommendation recommendation) {
        return RecommendationDTO.builder()
                .id(recommendation.getId())
                .userId(recommendation.getUser().getId())
                .careerId(recommendation.getCareer().getId())
                .careerName(recommendation.getCareer().getName())
                .matchScore(recommendation.getMatchScore())
                .reasoning(recommendation.getReasoning())
                .isActive(recommendation.getIsActive())
                .build();
    }
}