//package com.chanakya.service;
//
//import com.chanakya.dto.ProgressDataDTO;
//import com.chanakya.repository.AssessmentRepository;
//import com.chanakya.repository.LearningPathRepository;
//import com.chanakya.repository.ResourceRepository;
//import com.chanakya.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//@Service
//@RequiredArgsConstructor
//public class ProgressTrackerServiceImpl implements ProgressTrackerService {
//
//    private final AssessmentRepository assessmentRepository;
//    private final LearningPathRepository learningPathRepository;
//    private final ResourceRepository resourceRepository;
//    private final UserRepository userRepository;
//
//    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;
//    private static final int HOURS_PER_ASSESSMENT = 5; // same constant you use in DashboardService
//
//    @Override
//    public ProgressDataDTO getProgressData(Long userId) {
//
//        // Validate user exists (consistent with your existing service pattern)
//        userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
//
//        ProgressDataDTO dto = new ProgressDataDTO();
//
//        // ── Assessments ──────────────────────────────────────────────────
//        long totalAssessments  = assessmentRepository.count();          // system-wide total
//        int  completedByUser   = assessmentRepository.countByUserId(userId);
//
//        dto.setTotalAssessments((int) totalAssessments);
//        dto.setCompletedAssessments(completedByUser);
//
//        // ── Learning Paths ───────────────────────────────────────────────
//        dto.setEnrolledLearningPaths(learningPathRepository.countEnrolledByUserId(userId));
//        dto.setCompletedLearningPaths(learningPathRepository.countCompletedByUserId(userId));
//
//        // ── Resources ────────────────────────────────────────────────────
//        dto.setTotalResourcesViewed(resourceRepository.countViewedByUserId(userId));
//        dto.setBookmarkedResources(resourceRepository.countBookmarkedByUserId(userId));
//
//        // ── Streak ───────────────────────────────────────────────────────
//        dto.setCurrentStreak(calculateStreak(userId));
//
//        // ── Hours (reuses your existing formula) ─────────────────────────
//        dto.setTotalLearningHours(completedByUser * HOURS_PER_ASSESSMENT);
//
//        // ── Last activity date ────────────────────────────────────────────
//        String lastActivity = assessmentRepository
//                .findLastActivityByUserId(userId)
//                .map(dt -> dt.toLocalDate().format(DATE_FMT))
//                .orElse(LocalDate.now().format(DATE_FMT));  // fallback = today
//
//        dto.setLastActivityDate(lastActivity);
//
//        return dto;
//    }
//
//    // ─────────────────────────────────────────────────────────────────────
//    // Streak: counts how many consecutive days (up to today) the user
//    // completed at least one assessment.
//    //
//    // Uses a simple day-by-day walk from yesterday backwards.
//    // Replace with your own activity table query if you have one.
//    // ─────────────────────────────────────────────────────────────────────
//    private int calculateStreak(Long userId) {
//        // Fetch all distinct activity dates in descending order
//        // You can replace this with a proper @Query on UserActivity entity
//        // once you have that table. For now it derives from assessments.
//        java.util.List<LocalDateTime> activities =
//                assessmentRepository.findAllByUserIdOrderByCompletedAtDesc(userId)
//                        .stream()
//                        .map(a -> a.getCompletedAt())
//                        .filter(java.util.Objects::nonNull)
//                        .collect(java.util.stream.Collectors.toList());
//
//        if (activities.isEmpty()) return 0;
//
//        java.util.Set<LocalDate> activeDays = activities.stream()
//                .map(LocalDateTime::toLocalDate)
//                .collect(java.util.stream.Collectors.toSet());
//
//        int streak = 0;
//        LocalDate cursor = LocalDate.now();
//
//        while (activeDays.contains(cursor)) {
//            streak++;
//            cursor = cursor.minusDays(1);
//        }
//
//        return streak;
//    }
//}