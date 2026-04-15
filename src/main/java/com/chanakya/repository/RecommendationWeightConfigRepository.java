package com.chanakya.repository;

import com.chanakya.entity.RecommendationWeightConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationWeightConfigRepository extends JpaRepository<RecommendationWeightConfig, Long> {

    Optional<RecommendationWeightConfig> findByBucketName(String bucketName);

    List<RecommendationWeightConfig> findByIsActiveTrueOrderByBucketName();

    List<RecommendationWeightConfig> findAllByOrderByBucketName();
}
