package com.chanakya.repository;

import com.chanakya.entity.UserProgress;
import com.chanakya.entity.UserStepProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserProgressRepository
        extends JpaRepository<UserProgress, Long> {

    Optional<UserProgress> findByUserIdAndLearningStepId(Long userId, Long learningStepId);

    List<UserProgress> findByUserId(Long userId);

    @Query(value = """
    SELECT COUNT(*)
    FROM user_progress up
    JOIN learning_steps ls ON up.learning_step_id = ls.id
    WHERE up.user_id = :userId
    AND up.status = 'completed'
    AND ls.learning_path_id = :pathId
""", nativeQuery = true)
    long countCompletedStepsByUserAndPath(Long userId, Long pathId);
}
