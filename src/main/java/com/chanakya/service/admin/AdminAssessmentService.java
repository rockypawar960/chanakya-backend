package com.chanakya.service.admin;

import com.chanakya.dto.admin.*;
import com.chanakya.entity.Assessment;
import com.chanakya.entity.User;
import com.chanakya.repository.AssessmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminAssessmentService {

    private final AssessmentRepository assessmentRepository;

    @Transactional(readOnly = true)
    public PaginatedResponse<AdminAssessmentDTO> getAllAssessments(Pageable pageable) {
        Page<Assessment> assessmentPage = assessmentRepository.findAll(pageable);

        List<AdminAssessmentDTO> assessmentDTOs = assessmentPage.getContent()
                .stream()
                .map(this::convertToAdminAssessmentDTO)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(
                assessmentDTOs,
                assessmentPage.getNumber(),
                assessmentPage.getSize(),
                assessmentPage.getTotalElements(),
                assessmentPage.getTotalPages(),
                assessmentPage.isLast(),
                assessmentPage.isEmpty()
        );
    }

    @Transactional(readOnly = true)
    public AdminAssessmentDetailsDTO getAssessmentDetails(Long assessmentId) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
        return convertToAdminAssessmentDetailsDTO(assessment);
    }

    @Transactional
    public AdminAssessmentDTO updateAssessmentStatus(UpdateAssessmentStatusRequest request) {
        Assessment assessment = assessmentRepository.findById(request.getAssessmentId())
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));

        assessment.setIsActive(Boolean.TRUE.equals(request.getIsActive()));
        return convertToAdminAssessmentDTO(assessmentRepository.save(assessment));
    }

    @Transactional
    public void deleteAssessment(Long assessmentId) {
        if (!assessmentRepository.existsById(assessmentId)) {
            throw new EntityNotFoundException("Assessment not found");
        }
        assessmentRepository.deleteById(assessmentId);
    }

    @Transactional(readOnly = true)
    public AdminAssessmentAnalyticsDTO getAssessmentAnalytics() {
        List<Assessment> all = assessmentRepository.findAll();

        if (all.isEmpty()) {
            return buildEmptyAnalytics();
        }

        double avg = all.stream()
                .mapToDouble(a -> Optional.ofNullable(a.getTotalScore()).orElse(0))
                .average()
                .orElse(0);

        LocalDateTime now = LocalDateTime.now();
        Map<String, Long> ranges = calculateScoreRanges(all);

        return AdminAssessmentAnalyticsDTO.builder()
                .totalAssessments((long) all.size())
                .activeAssessments(all.stream().filter(a -> Boolean.TRUE.equals(a.getIsActive())).count())
                .inactiveAssessments(all.stream().filter(a -> !Boolean.TRUE.equals(a.getIsActive())).count())
                .averageScore(round(avg))
                .assessmentsBelowAverage(all.stream().filter(a -> safeScore(a) < avg).count())
                .assessmentsAboveAverage(all.stream().filter(a -> safeScore(a) > avg).count())
                .assessmentsAtAverage(all.stream().filter(a -> safeScore(a) == Math.round(avg)).count())
                .assessmentsThisWeek(countByDate(all, now.minusDays(7)))
                .assessmentsThisMonth(countByDate(all, now.minusDays(30)))
                .assessmentsThisYear(countByDate(all, now.minusDays(365)))
                .averageBucketScores(calculateAverageBucketScores(all))
                .topAssessments(all.stream()
                        .sorted(Comparator.comparingInt(this::safeScore).reversed())
                        .limit(5)
                        .map(this::convertToAdminAssessmentDTO)
                        .collect(Collectors.toList()))
                .scoreRange0to20(ranges.getOrDefault("0-20", 0L))
                .scoreRange20to40(ranges.getOrDefault("20-40", 0L))
                .scoreRange40to60(ranges.getOrDefault("40-60", 0L))
                .scoreRange60to80(ranges.getOrDefault("60-80", 0L))
                .scoreRange80to100(ranges.getOrDefault("80-100", 0L))
                .build();
    }

    // ================= HELPER METHODS =================

    private int safeScore(Assessment a) {
        return Optional.ofNullable(a.getTotalScore()).orElse(0);
    }

    private long countByDate(List<Assessment> list, LocalDateTime date) {
        return list.stream()
                .filter(a -> a.getCompletedAt() != null && a.getCompletedAt().isAfter(date))
                .count();
    }

    private double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    private Map<String, Long> calculateScoreRanges(List<Assessment> assessments) {
        Map<String, Long> ranges = new HashMap<>();
        ranges.put("0-20", 0L);
        ranges.put("20-40", 0L);
        ranges.put("40-60", 0L);
        ranges.put("60-80", 0L);
        ranges.put("80-100", 0L);

        for (Assessment a : assessments) {
            int score = safeScore(a);
            if (score >= 0 && score < 20) ranges.put("0-20", ranges.get("0-20") + 1);
            else if (score >= 20 && score < 40) ranges.put("20-40", ranges.get("20-40") + 1);
            else if (score >= 40 && score < 60) ranges.put("40-60", ranges.get("40-60") + 1);
            else if (score >= 60 && score < 80) ranges.put("60-80", ranges.get("60-80") + 1);
            else if (score >= 80 && score <= 100) ranges.put("80-100", ranges.get("80-100") + 1);
        }
        return ranges;
    }

    private AdminAssessmentDTO convertToAdminAssessmentDTO(Assessment a) {
        User u = a.getUser();
        return AdminAssessmentDTO.builder()
                .id(a.getId())
                .userId(u != null ? u.getId() : null)
                .userEmail(u != null ? u.getEmail() : "N/A")
                .userFullName(u != null ? u.getFirstName() + " " + u.getLastName() : "N/A")
                .totalScore(safeScore(a))
                .completedAt(a.getCompletedAt())
                .isActive(Boolean.TRUE.equals(a.getIsActive()))
                .bucketScores(convertBucketScores(a.getBucketScores()))
                .build();
    }

    private Map<String, Integer> convertBucketScores(Map<String, Integer> input) {
        return input == null ? new HashMap<>() : input;
    }

    private AdminAssessmentDetailsDTO convertToAdminAssessmentDetailsDTO(Assessment a) {
        User u = a.getUser();
        return AdminAssessmentDetailsDTO.builder()
                .id(a.getId())
                .userId(u != null ? u.getId() : null)
                .userEmail(u != null ? u.getEmail() : "N/A")
                .userFullName(u != null ? u.getFirstName() + " " + u.getLastName() : "N/A")
                .totalScore(safeScore(a))
                .completedAt(a.getCompletedAt())
                .isActive(Boolean.TRUE.equals(a.getIsActive()))
                .bucketScores(Optional.ofNullable(a.getBucketScores()).orElse(new HashMap<>()))
                .rawResponses(a.getAnswers())
                .build();
    }

    private Map<String, Double> calculateAverageBucketScores(List<Assessment> list) {
        Map<String, List<Double>> map = new HashMap<>();

        for (Assessment a : list) {
            if (a.getBucketScores() == null) continue;
            a.getBucketScores().forEach((k, v) -> {
                map.computeIfAbsent(k, x -> new ArrayList<>()).add(v.doubleValue());
            });
        }

        Map<String, Double> result = new HashMap<>();
        map.forEach((k, v) -> result.put(k, round(v.stream().mapToDouble(d -> d).average().orElse(0))));
        return result;
    }

    private AdminAssessmentAnalyticsDTO buildEmptyAnalytics() {
        return AdminAssessmentAnalyticsDTO.builder()
                .totalAssessments(0L).activeAssessments(0L).inactiveAssessments(0L)
                .averageScore(0.0).assessmentsBelowAverage(0L).assessmentsAboveAverage(0L)
                .assessmentsAtAverage(0L).averageBucketScores(new HashMap<>())
                .assessmentsThisWeek(0L).assessmentsThisMonth(0L).assessmentsThisYear(0L)
                .topAssessments(new ArrayList<>())
                .scoreRange0to20(0L).scoreRange20to40(0L).scoreRange40to60(0L)
                .scoreRange60to80(0L).scoreRange80to100(0L)
                .build();
    }
}