package com.chanakya.service;

import com.chanakya.entity.Recommendation;
import com.chanakya.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromptBuilderService {

    private final RecommendationRepository recommendationRepository;

    /**
     * Fetches recommendation from DB using userId + careerId
     * and builds a rich prompt for Gemini automatically.
     * Frontend sends NOTHING except userId + careerId.
     */
    public String buildPrompt(Long userId, Long careerId) {

        // Fetch recommendation from DB
        Recommendation rec = recommendationRepository
                .findByUserIdAndIsActiveTrueOrderByMatchScoreDesc(userId)
                .stream()
                .filter(r -> r.getCareer().getId().equals(careerId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "No active recommendation found for userId=" + userId + " careerId=" + careerId));

        String careerName  = rec.getCareer().getName();
        Double matchScore  = rec.getMatchScore();
        String reasoning   = rec.getReasoning();

        // Also pull assessment bucket scores if available
        String bucketInfo = "";
        if (rec.getAssessment() != null && rec.getAssessment().getBucketScores() != null) {
            bucketInfo = "\nStudent Skill Scores by Category: " + rec.getAssessment().getBucketScores().toString();
        }

        // Build the prompt — Gemini receives this
        String prompt = """
                Career Goal: %s
                Match Score: %.1f%%
                Student Analysis: %s
                %s
                
                Based on this student's profile, generate a complete personalized learning roadmap.
                The roadmap must:
                - Start from the student's current level (infer from match score and reasoning)
                - Focus specifically on the "%s" career path
                - Cover all skills needed to be job-ready
                - Include practical tasks and video resources for each step
                """.formatted(careerName, matchScore, reasoning, bucketInfo, careerName);

        log.info("Built prompt for userId={} careerId={} career={}", userId, careerId, careerName);
        log.debug("Prompt content: {}", prompt);

        return prompt;
    }
}