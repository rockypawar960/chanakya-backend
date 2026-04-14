package com.chanakya.repository;

import com.chanakya.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByUserIdAndIsActiveTrue(Long userId);
    List<Recommendation> findByAssessmentIdAndIsActiveTrue(Long assessmentId);
    List<Recommendation> findByAssessmentIdAndIsActiveTrueOrderByMatchScoreDesc(Long assessmentId);
    List<Recommendation> findByUserIdAndIsActiveTrueOrderByMatchScoreDesc(Long userId);

    void deleteByAssessmentId(Long id);
}