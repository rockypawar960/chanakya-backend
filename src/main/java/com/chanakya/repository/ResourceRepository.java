package com.chanakya.repository;

import com.chanakya.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    // ===== PAGINATED METHODS (For Service) =====

    // By career with pagination
    Page<Resource> findByCareerId(Long careerId, Pageable pageable);

    // By resource type with pagination
    Page<Resource> findByResourceType(String resourceType, Pageable pageable);

    // By career and resource type with pagination
    Page<Resource> findByCareerIdAndResourceType(Long careerId, String resourceType, Pageable pageable);

    // Active resources by career with pagination
    Page<Resource> findByCareerIdAndIsActiveTrue(Long careerId, Pageable pageable);

    // ===== LIST METHODS (For internal use) =====

    // By career (returns list)
    List<Resource> findByCareerIdAndIsActiveTrueOrderByCreatedAtDesc(Long careerId);

    // By resource type (returns list)
    List<Resource> findByResourceTypeAndIsActiveTrueOrderByCreatedAtDesc(String resourceType);

    // All active resources (returns list)
    List<Resource> findByIsActiveTrueOrderByCreatedAtDesc();

    // By career and resource type (returns list)
    List<Resource> findByCareerIdAndResourceTypeAndIsActiveTrueOrderByCreatedAtDesc(Long careerId, String resourceType);

    // ===== HELPER METHODS =====

    // Check if career has any resources
    boolean existsByCareerId(Long careerId);

    // Count by career
    long countByCareerId(Long careerId);
}