package com.chanakya.repository;

import com.chanakya.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerRepository extends JpaRepository<Career, Long> {

    // 🔥 For CareerService - get all active careers
    List<Career> findByIsActiveTrueOrderByPopularityScoreDesc();

    // 🔥 For RecommendationService - get top 5 careers
    List<Career> findTop5ByIsActiveTrueOrderByPopularityScoreDesc();

    Optional<Career> findByNameAndIsActiveTrue(String name);
}