package com.chanakya.repository;

import com.chanakya.entity.UserProgress;
import com.chanakya.entity.UserStepProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProgressRepository
        extends JpaRepository<UserProgress, Long> {

    Optional<UserProgress> findByUserIdAndLearningStepId(Long userId, Long learningStepId);

    List<UserProgress> findByUserId(Long userId);

    @Query("SELECT COUNT(up) FROM UserProgress up " +
            "JOIN LearningStep ls ON up.learningStepId = ls.id " +
            "WHERE up.userId = :userId AND ls.learningPath.id = :pathId AND up.status = 'completed'")
    long countCompletedStepsByUserAndPath(
            @Param("userId") Long userId,
            @Param("pathId") Long pathId
    );

    // ✅ Fix: Manual Query using JOIN to find pathId through learningStep
    @Query("SELECT COUNT(up) > 0 FROM UserProgress up " +
            "JOIN LearningStep ls ON up.learningStepId = ls.id " +
            "WHERE up.userId = :userId AND ls.learningPath.id = :pathId AND up.status = :status")
    boolean existsByUserIdAndPathIdAndStatus(
            @Param("userId") Long userId,
            @Param("pathId") Long pathId,
            @Param("status") String status
    );

    @Query("SELECT COUNT(up) FROM UserProgress up " +
            "JOIN LearningStep ls ON up.learningStepId = ls.id " +
            "WHERE up.userId = :userId AND ls.learningPath.id = :pathId " +
            "AND (up.status = 'in_progress' OR up.status = 'completed' OR up.status = 'IN_PROGRESS')")
    long countStartedOrCompletedSteps(
            @Param("userId") Long userId,
            @Param("pathId") Long pathId
    );
}
