package com.chanakya.repository;

import com.chanakya.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    Optional<Assessment> findTopByUserIdAndIsActiveTrueOrderByCompletedAtDesc(Long userId);

    List<Assessment> findByUserIdAndIsActiveTrueOrderByCompletedAtDesc(Long userId);
}