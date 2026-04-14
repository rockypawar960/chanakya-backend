package com.chanakya.repository;

import com.chanakya.entity.LearningStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningStepRepository extends JpaRepository<LearningStep, Long> {

    List<LearningStep> findByLearningPathIdOrderByStepOrder(Long pathId);

    @Query(value = """
    SELECT COUNT(*)
    FROM learning_steps
    WHERE learning_path_id = :pathId
""", nativeQuery = true)
    long countStepsByPathId(Long pathId);
}