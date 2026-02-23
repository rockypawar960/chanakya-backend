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

        Map<String, Integer> userBuckets = assessment.getBucketScores();

        List<Career> careers =
                careerRepository.findTop5ByIsActiveTrueOrderByPopularityScoreDesc();

        List<Recommendation> recommendations = new ArrayList<>();

        for (Career career : careers) {

            double matchScore = calculateMatchScore(userBuckets, career);

            Recommendation recommendation = Recommendation.builder()
                    .user(assessment.getUser())
                    .assessment(assessment)
                    .career(career)
                    .matchScore(matchScore)
                    .reasoning(generateReasoning(userBuckets))
                    .isActive(true)
                    .build();

            recommendations.add(recommendation);
        }

        // Sort by highest score
        recommendations.sort((a, b) ->
                Double.compare(b.getMatchScore(), a.getMatchScore()));

        // Keep top 3 only
        recommendations = recommendations.stream().limit(3).toList();

        recommendationRepository.saveAll(recommendations);

        return recommendations.stream().map(this::mapToDTO).toList();
    }


    public List<RecommendationDTO> getRecommendationsByAssessmentId(Long assessmentId) {

        List<Recommendation> recommendations =
                recommendationRepository
                        .findByAssessmentIdAndIsActiveTrueOrderByMatchScoreDesc(assessmentId);

        return recommendations.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<RecommendationDTO> getRecommendationsByUserId(Long userId) {

        List<Recommendation> recommendations =
                recommendationRepository
                        .findByUserIdAndIsActiveTrueOrderByMatchScoreDesc(userId);

        return recommendations.stream()
                .map(this::mapToDTO)
                .toList();
    }
    private double calculateMatchScore(Map<String, Integer> userBuckets, Career career) {

        List<CareerAttribute> attributes =
                careerAttributeRepository.findByCareerIdAndIsActiveTrue(career.getId());

        double score = 0;
        double maxScore = 0;

        for (CareerAttribute attr : attributes) {

            String bucketName = attr.getBucket().getName();
            int weight = attr.getWeight();

            int userScore = userBuckets.getOrDefault(bucketName, 0);

            score += userScore * weight;
            maxScore += 100 * weight;
        }

        if (maxScore == 0) return 0;

        return (score / maxScore) * 100;
    }

    private String generateReasoning(Map<String, Integer> buckets) {
        return "This career aligns strongly with your dominant interest areas.";
    }

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
}