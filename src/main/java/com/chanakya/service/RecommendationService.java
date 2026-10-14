package com.chanakya.service;

import com.chanakya.dto.RecommendationDTO;
import com.chanakya.entity.*;
import com.chanakya.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecommendationService {

    private final CareerRepository careerRepository;
    private final RecommendationRepository recommendationRepository;
    private final CareerAttributeRepository careerAttributeRepository;

    public List<RecommendationDTO> generateRecommendations(Assessment assessment) {

        // 1. Purani recommendations hatao (duplicate rows / unique constraint se bachne ke liye)
        recommendationRepository.deleteByAssessmentId(assessment.getId());
        recommendationRepository.flush(); // delete DB me pehle pahunche, phir naye insert

        // 2. Bucket keys ko uppercase me normalize karo (lookup mismatch na ho)
        Map<String, Integer> userBuckets = normalizeBuckets(assessment.getBucketScores());

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
                    .reasoning(buildReasoning(userBuckets))
                    .isActive(true)
                    .build());
        }

        // 3. Sorting & Limiting (Top 3)
        List<Recommendation> topRecommendations = recommendations.stream()
                .sorted((r1, r2) -> Double.compare(r2.getMatchScore(), r1.getMatchScore()))
                .limit(3)
                .toList();

        List<Recommendation> saved = recommendationRepository.saveAll(topRecommendations);
        return saved.stream().map(this::mapToDTO).toList();
    }

    private double calculateMatchScore(Map<String, Integer> userBuckets, Career career) {
        if (userBuckets == null || userBuckets.isEmpty()) return 0.0;

        List<CareerAttribute> attributes =
                careerAttributeRepository.findByCareerIdAndIsActiveTrue(career.getId());
        if (attributes == null || attributes.isEmpty()) return 0.0;

        double weightedScore = 0.0;
        double totalWeight = 0.0;

        for (CareerAttribute attr : attributes) {
            if (attr.getBucket() == null) continue;

            String bucketName = attr.getBucket().getName().toUpperCase().trim();
            double weight = (double) attr.getWeight();

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

    /** Bucket names ko uppercase + trim karke naya map banata hai */
    private Map<String, Integer> normalizeBuckets(Map<String, Integer> raw) {
        Map<String, Integer> result = new HashMap<>();
        if (raw == null) return result;

        raw.forEach((key, value) -> {
            if (key != null && value != null) {
                result.put(key.toUpperCase().trim(), value);
            }
        });
        return result;
    }

    /** Sirf user ke top 3 strong buckets reasoning me dikhao */
    private String buildReasoning(Map<String, Integer> userBuckets) {
        String topBuckets = userBuckets.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));

        return "Based on your strengths in " + topBuckets;
    }

    /** Entity ko DTO mein badalne ke liye helper method */
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

    // Controller ke liye
    public List<RecommendationDTO> getRecommendationsByAssessmentId(Long assessmentId) {
        return recommendationRepository
                .findByAssessmentIdAndIsActiveTrueOrderByMatchScoreDesc(assessmentId)
                .stream().map(this::mapToDTO).toList();
    }

    // Controller ke liye
    public List<RecommendationDTO> getRecommendationsByUserId(Long userId) {
        return recommendationRepository
                .findByUserIdAndIsActiveTrueOrderByMatchScoreDesc(userId)
                .stream().map(this::mapToDTO).toList();
    }
}