package com.chanakya.repository;

import com.chanakya.entity.LearningPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

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
}