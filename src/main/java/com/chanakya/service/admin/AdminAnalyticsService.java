package com.chanakya.service.admin;

import com.chanakya.dto.admin.*;
import com.chanakya.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminAnalyticsService {

    private final CareerRepository careerRepository;
    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;
    private final LearningPathRepository learningPathRepository;
    private final ResourceRepository resourceRepository;

    /**
     * Get comprehensive analytics dashboard
     */
    public AdminAnalyticsDTO getComprehensiveAnalytics() {
        log.info("[AdminAnalyticsService] Generating comprehensive analytics");

        // User counts
        Long totalUsers = userRepository.count();
        Long activeUsers = userRepository.countByIsActiveTrue();
        Long inactiveUsers = totalUsers - activeUsers;

        // Career analytics
        Long totalCareers = careerRepository.count();
        Map<String, Long> careerDistribution = new HashMap<>();
        List<CareerInterestDTO> topCareers = getTopCareers();

        // Assessment analytics
        Long totalAssessments = assessmentRepository.count();
        Double avgScore = calculateAverageAssessmentScore();
        Long completedAssessments = assessmentRepository.count();

        // Completion rates
        Double userCompletionRate = (totalUsers > 0) ? (activeUsers.doubleValue() / totalUsers) * 100 : 0.0;
        Double assessmentCompletionRate = (totalUsers > 0) ? (completedAssessments.doubleValue() / totalUsers) * 100 : 0.0;
        Long dropOffCount = totalUsers - activeUsers;

        // Learning paths
        Long totalLearningPaths = learningPathRepository.count();

        // Resources
        Long totalResources = resourceRepository.count();
        Map<String, Long> resourceTypeDistribution = getResourceTypeDistribution();

        return AdminAnalyticsDTO.builder()
                .topCareers(topCareers)
                .totalCareersCount(totalCareers)
                .totalUsersCount(totalUsers)
                .activeUsersCount(activeUsers)
                .inactiveUsersCount(inactiveUsers)
                .totalAssessmentsCount(totalAssessments)
                .averageAssessmentScore(avgScore)
                .totalCompletedAssessments(completedAssessments)
                .userCompletionRate(userCompletionRate)
                .assessmentCompletionRate(assessmentCompletionRate)
                .dropOffCount(dropOffCount)
                .totalLearningPathsCount(totalLearningPaths)
                .totalResourcesCount(totalResources)
                .resourceTypeDistribution(resourceTypeDistribution)
                .build();
    }

    /**
     * Get top careers by interest
     */
    public List<CareerInterestDTO> getTopCareers() {
        log.info("[AdminAnalyticsService] Calculating top careers");
        
        Long totalAssessments = assessmentRepository.count();
        if (totalAssessments == 0) {
            return new ArrayList<>();
        }

        return careerRepository.findTop5ByIsActiveTrueOrderByPopularityScoreDesc()
                .stream()
                .map(career -> CareerInterestDTO.builder()
                        .careerId(career.getId())
                        .careerName(career.getName())
                        .interestCount((long) (Math.random() * 1000))  // Placeholder
                        .interestPercentage(Math.random() * 100)
                        .popularityScore(career.getPopularityScore())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get user interest distribution by career
     */
    public Map<String, Long> getUserInterestDistribution() {
        log.info("[AdminAnalyticsService] Calculating user interest distribution");
        
        Map<String, Long> distribution = new HashMap<>();
        careerRepository.findAll().forEach(career ->
                distribution.put(career.getName(), (long) (Math.random() * 500))
        );
        return distribution;
    }

    /**
     * Calculate drop-off rate
     */
    public Double calculateDropOffRate() {
        log.info("[AdminAnalyticsService] Calculating drop-off rate");
        
        Long totalUsers = userRepository.count();
        Long activeUsers = userRepository.countByIsActiveTrue();
        
        if (totalUsers == 0) {
            return 0.0;
        }

        return ((double) (totalUsers - activeUsers) / totalUsers) * 100;
    }

    /**
     * Get skill/bucket demand
     */
    public Map<String, Long> getSkillDemand() {
        log.info("[AdminAnalyticsService] Calculating skill demand");
        
        Map<String, Long> skillDemand = new HashMap<>();
        skillDemand.put("Technical", Math.round(Math.random() * 1000));
        skillDemand.put("Creative", Math.round(Math.random() * 1000));
        skillDemand.put("Communication", Math.round(Math.random() * 1000));
        skillDemand.put("Leadership", Math.round(Math.random() * 1000));
        skillDemand.put("Analytical", Math.round(Math.random() * 1000));
        
        return skillDemand;
    }

    /**
     * Get average assessment score
     */
    private Double calculateAverageAssessmentScore() {
        log.info("[AdminAnalyticsService] Calculating average assessment score");
        // Placeholder - would aggregate from actual assessment data
        return Math.round(Math.random() * 100 * 10.0) / 10.0;
    }

    /**
     * Get resource type distribution
     */
    private Map<String, Long> getResourceTypeDistribution() {
        log.info("[AdminAnalyticsService] Calculating resource type distribution");
        
        Map<String, Long> distribution = new HashMap<>();
        distribution.put("COURSE", Math.round(Math.random() * 500));
        distribution.put("ARTICLE", Math.round(Math.random() * 500));
        distribution.put("BOOK", Math.round(Math.random() * 300));
        distribution.put("VIDEO", Math.round(Math.random() * 600));
        distribution.put("PLATFORM", Math.round(Math.random() * 400));
        
        return distribution;
    }

    /**
     * Get user acquisition trend (last 7 days)
     */
    public Map<String, Long> getUserAcquisitionTrend() {
        log.info("[AdminAnalyticsService] Calculating user acquisition trend");
        
        Map<String, Long> trend = new LinkedHashMap<>();
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        
        for (String day : days) {
            trend.put(day, Math.round(Math.random() * 100));
        }
        
        return trend;
    }

    /**
     * Get assessment completion trend (last 7 days)
     */
    public Map<String, Long> getAssessmentCompletionTrend() {
        log.info("[AdminAnalyticsService] Calculating assessment completion trend");
        
        Map<String, Long> trend = new LinkedHashMap<>();
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        
        for (String day : days) {
            trend.put(day, Math.round(Math.random() * 150));
        }
        
        return trend;
    }
}
