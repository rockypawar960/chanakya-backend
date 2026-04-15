package com.chanakya.service.admin;

import com.chanakya.dto.admin.AdminDashboardDTO;
import com.chanakya.entity.*;
import com.chanakya.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;
    private final CareerRepository careerRepository;
    private final RecommendationRepository recommendationRepository;

    /**
     * Get complete dashboard statistics
     */
    public AdminDashboardDTO getDashboardStats() {
        log.info("Fetching dashboard statistics");
        
        return AdminDashboardDTO.builder()
                .totalUsers(getTotalUsers())
                .totalAssessments(getTotalAssessments())
                .activeUsersDaily(getActiveUsersDaily())
                .activeUsersWeekly(getActiveUsersWeekly())
                .assessmentCompletionRate(getAssessmentCompletionRate())
                .topCareers(getTopCareers())
                .careerPopularityDistribution(getCareerPopularityDistribution())
                .userRegistrationTrend(getUserRegistrationTrend())
                .assessmentCompletionTrend(getAssessmentCompletionTrend())
                .build();
    }

    /**
     * Total count of active users in the system
     */
    public Long getTotalUsers() {
        try {
            List<User> allUsers = userRepository.findAll();
            long totalActiveUsers = allUsers.stream()
                    .filter(User::getIsActive)
                    .count();
            log.debug("Total active users: {}", totalActiveUsers);
            return totalActiveUsers;
        } catch (Exception e) {
            log.error("Error fetching total users", e);
            return 0L;
        }
    }

    /**
     * Total count of completed assessments
     */
    public Long getTotalAssessments() {
        try {
            List<Assessment> assessments = assessmentRepository.findAll();
            long totalCompleted = assessments.stream()
                    .filter(Assessment::getIsActive)
                    .count();
            log.debug("Total active assessments: {}", totalCompleted);
            return totalCompleted;
        } catch (Exception e) {
            log.error("Error fetching total assessments", e);
            return 0L;
        }
    }

    /**
     * Users who logged in in the last 24 hours
     */
    public Long getActiveUsersDaily() {
        try {
            LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
            List<User> allUsers = userRepository.findAll();
            
            long activeDaily = allUsers.stream()
                    .filter(user -> user.getLastLogin() != null && 
                            user.getLastLogin().isAfter(oneDayAgo) &&
                            user.getIsActive())
                    .count();
            
            log.debug("Active users in last 24h: {}", activeDaily);
            return activeDaily;
        } catch (Exception e) {
            log.error("Error fetching daily active users", e);
            return 0L;
        }
    }

    /**
     * Users who logged in in the last 7 days
     */
    public Long getActiveUsersWeekly() {
        try {
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
            List<User> allUsers = userRepository.findAll();
            
            long activeWeekly = allUsers.stream()
                    .filter(user -> user.getLastLogin() != null && 
                            user.getLastLogin().isAfter(sevenDaysAgo) &&
                            user.getIsActive())
                    .count();
            
            log.debug("Active users in last 7 days: {}", activeWeekly);
            return activeWeekly;
        } catch (Exception e) {
            log.error("Error fetching weekly active users", e);
            return 0L;
        }
    }

    /**
     * Assessment completion rate (%)
     */
    public Double getAssessmentCompletionRate() {
        try {
            List<User> allActiveUsers = userRepository.findAll().stream()
                    .filter(User::getIsActive)
                    .toList();
            
            if (allActiveUsers.isEmpty()) {
                return 0.0;
            }
            
            List<Assessment> assessments = assessmentRepository.findAll();
            Set<Long> usersWithAssessment = assessments.stream()
                    .filter(Assessment::getIsActive)
                    .map(a -> a.getUser().getId())
                    .collect(Collectors.toSet());
            
            double rate = (double) usersWithAssessment.size() / allActiveUsers.size() * 100;
            log.debug("Assessment completion rate: {}%", rate);
            return Math.round(rate * 100.0) / 100.0;
        } catch (Exception e) {
            log.error("Error calculating completion rate", e);
            return 0.0;
        }
    }

    /**
     * Get top 5 most popular careers with recommendation counts
     */
    public List<AdminDashboardDTO.CareerStatsDTO> getTopCareers() {
        try {
            List<Career> topCareers = careerRepository.findTop5ByIsActiveTrueOrderByPopularityScoreDesc();
            
            return topCareers.stream()
                    .map(career -> {
                        Long recommendationCount = getRecommendationCountForCareer(career.getId());
                        return AdminDashboardDTO.CareerStatsDTO.builder()
                                .careerId(career.getId())
                                .careerName(career.getName())
                                .popularityScore(career.getPopularityScore())
                                .recommendationCount(recommendationCount)
                                .build();
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching top careers", e);
            return new ArrayList<>();
        }
    }

    /**
     * Get career popularity distribution across all active careers
     */
    public Map<String, Integer> getCareerPopularityDistribution() {
        try {
            List<Career> allCareers = careerRepository.findByIsActiveTrueOrderByPopularityScoreDesc();
            
            return allCareers.stream()
                    .collect(Collectors.toMap(
                            Career::getName,
                            Career::getPopularityScore,
                            Integer::sum
                    ));
        } catch (Exception e) {
            log.error("Error fetching career popularity distribution", e);
            return new HashMap<>();
        }
    }

    /**
     * User registration trend for last 7 days
     */
    public List<AdminDashboardDTO.DailyTrendDTO> getUserRegistrationTrend() {
        try {
            List<AdminDashboardDTO.DailyTrendDTO> trend = new ArrayList<>();
            List<User> allUsers = userRepository.findAll();
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            
            for (int i = 6; i >= 0; i--) {
                LocalDateTime dayStart = now.minusDays(i).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime dayEnd = dayStart.withHour(23).withMinute(59).withSecond(59);
                String dateStr = dayStart.format(formatter);
                
                long count = allUsers.stream()
                        .filter(u -> u.getCreatedAt() != null &&
                                u.getCreatedAt().isAfter(dayStart) &&
                                u.getCreatedAt().isBefore(dayEnd))
                        .count();
                
                trend.add(AdminDashboardDTO.DailyTrendDTO.builder()
                        .date(dateStr)
                        .count(count)
                        .build());
            }
            
            log.debug("User registration trend: {} days", trend.size());
            return trend;
        } catch (Exception e) {
            log.error("Error fetching user registration trend", e);
            return new ArrayList<>();
        }
    }

    /**
     * Assessment completion trend for last 7 days
     */
    public List<AdminDashboardDTO.DailyTrendDTO> getAssessmentCompletionTrend() {
        try {
            List<AdminDashboardDTO.DailyTrendDTO> trend = new ArrayList<>();
            List<Assessment> allAssessments = assessmentRepository.findAll();
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            
            for (int i = 6; i >= 0; i--) {
                LocalDateTime dayStart = now.minusDays(i).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime dayEnd = dayStart.withHour(23).withMinute(59).withSecond(59);
                String dateStr = dayStart.format(formatter);
                
                long count = allAssessments.stream()
                        .filter(a -> a.getCompletedAt() != null &&
                                a.getCompletedAt().isAfter(dayStart) &&
                                a.getCompletedAt().isBefore(dayEnd) &&
                                a.getIsActive())
                        .count();
                
                trend.add(AdminDashboardDTO.DailyTrendDTO.builder()
                        .date(dateStr)
                        .count(count)
                        .build());
            }
            
            log.debug("Assessment completion trend: {} days", trend.size());
            return trend;
        } catch (Exception e) {
            log.error("Error fetching assessment completion trend", e);
            return new ArrayList<>();
        }
    }

    /**
     * Helper method to get recommendation count for a career
     */
    private Long getRecommendationCountForCareer(Long careerId) {
        try {
            List<Recommendation> recommendations = recommendationRepository.findAll();
            return recommendations.stream()
                    .filter(r -> r.getCareer().getId().equals(careerId) && r.getIsActive())
                    .count();
        } catch (Exception e) {
            log.error("Error fetching recommendation count for career: {}", careerId, e);
            return 0L;
        }
    }
}
