package com.chanakya.dto.admin;

import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardDTO {
    
    // Overall Statistics
    private Long totalUsers;
    private Long totalAssessments;
    private Long activeUsersDaily;
    private Long activeUsersWeekly;
    private Double assessmentCompletionRate;
    
    // Career Statistics
    private List<CareerStatsDTO> topCareers;
    private Map<String, Integer> careerPopularityDistribution;
    
    // Trend Data
    private List<DailyTrendDTO> userRegistrationTrend;
    private List<DailyTrendDTO> assessmentCompletionTrend;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CareerStatsDTO {
        private Long careerId;
        private String careerName;
        private Integer popularityScore;
        private Long recommendationCount;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DailyTrendDTO {
        private String date;
        private Long count;
    }
}
