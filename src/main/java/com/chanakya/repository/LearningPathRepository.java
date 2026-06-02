package com.chanakya.repository;

import com.chanakya.entity.LearningPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LearningPathRepository extends JpaRepository<LearningPath, Long> {

    // For paginated results
    Page<LearningPath> findByCareerId(Long careerId, Pageable pageable);

    // For all results (without pagination)
    List<LearningPath> findByCareerId(Long careerId);



    // For active learning paths only
    Page<LearningPath> findByCareerIdAndIsActiveTrue(Long careerId, Pageable pageable);

    // Count by career
    long countByCareerId(Long careerId);

//    // NEW: paths the user has enrolled in
//    @Query(value = """
//    SELECT COUNT(*)
//    FROM learning_paths
//    WHERE user_id = :userId
//      AND is_active = true
//    """, nativeQuery = true)
//    Optional<LearningPath> findActiveByCareerId(@Param("userId") Long careerId);
//
//    // NEW: paths the user has fully completed
//    @Query(value = """
//    SELECT COUNT(*)
//    FROM learning_paths
//    WHERE user_id = :userId
//      AND completed_at = true
//    """, nativeQuery = true)
//    int countCompletedByUserId(Long userId);

//    int countEnrolledByUserId(Long userId);
}