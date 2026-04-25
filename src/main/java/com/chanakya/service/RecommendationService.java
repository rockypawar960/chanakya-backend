package com.chanakya.service;

import com.chanakya.dto.RecommendationDTO;
import com.chanakya.entity.*;
import com.chanakya.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecommendationService {

    private final CareerRepository careerRepository;
    private final RecommendationRepository recommendationRepository;
    private final CareerAttributeRepository careerAttributeRepository;

    public List<RecommendationDTO> generateRecommendations(Assessment assessment) {
        // 1. Purani recommendations deactivate ya delete karein
//        recommendationRepository.deleteByAssessmentId(assessment.getId());

        Map<String, Integer> userBuckets = assessment.getBucketScores();
        List<Career> careers = careerRepository.findByIsActiveTrueOrderByPopularityScoreDesc();
        List<Recommendation> recommendations = new ArrayList<>();

        for (Career career : careers) {
            double matchScore = calculateMatchScore(userBuckets, career);
            if (matchScore <= 0) continue;

            recommendations.add(Recommendation.builder()
                    .user(assessment.getUser())
                    .assessment(assessment)
                    .career(career)
                    .matchScore(matchScore)
                    .reasoning("Based on your strengths in " + String.join(", ", userBuckets.keySet()))
                    .isActive(true)
                    .build());
        }

        // Sorting & Limiting (Top 3)
        List<Recommendation> topRecommendations = recommendations.stream()
                .sorted((r1, r2) -> Double.compare(r2.getMatchScore(), r1.getMatchScore()))
                .limit(3)
                .toList();

        recommendationRepository.saveAll(topRecommendations);
        return topRecommendations.stream().map(this::mapToDTO).toList();
    }

    private double calculateMatchScore(Map<String, Integer> userBuckets, Career career) {
        if (userBuckets == null || userBuckets.isEmpty()) return 0.0;

        List<CareerAttribute> attributes = careerAttributeRepository.findByCareerIdAndIsActiveTrue(career.getId());
        if (attributes == null || attributes.isEmpty()) return 0.0;

        double weightedScore = 0.0;
        double totalWeight = 0.0;

        for (CareerAttribute attr : attributes) {
            if (attr.getBucket() == null) continue;

            String bucketName = attr.getBucket().getName().toUpperCase().trim();
            double weight = (double) attr.getWeight(); // Use double for weight too

            int userScore = userBuckets.getOrDefault(bucketName, 0);

            weightedScore += userScore * weight;
            totalWeight += weight;
        }

        if (totalWeight == 0) return 0.0;

        // Formula: (Total Obtained / Total Possible) * 100
        // Assuming each bucket max score is 10
        double score = (weightedScore / (totalWeight * 10.0)) * 100.0;

        return Math.min(score, 100.0); // 100 se upar na jaye
    }

    /**
     * 🔄 Entity ko DTO mein badalne ke liye helper method
     */
    private RecommendationDTO mapToDTO(Recommendation r) {
        return RecommendationDTO.builder()
                .id(r.getId())
                .careerId(r.getCareer().getId())
                .careerName(r.getCareer().getName())
                .matchScore(r.getMatchScore())
                .reasoning(r.getReasoning())
                .isActive(r.getIsActive())
                .build();
    }

    // 1. Ye method Controller mang raha hai
    @Transactional
    public List<RecommendationDTO> getRecommendationsByAssessmentId(Long assessmentId) {
        return recommendationRepository.findByAssessmentIdAndIsActiveTrueOrderByMatchScoreDesc(assessmentId)
                .stream().map(this::mapToDTO).toList();
    }

    // 2. Ye method bhi Controller mang raha hai
    public List<RecommendationDTO> getRecommendationsByUserId(Long userId) {
        return recommendationRepository.findByUserIdAndIsActiveTrueOrderByMatchScoreDesc(userId)
                .stream().map(this::mapToDTO).toList();
    }


}