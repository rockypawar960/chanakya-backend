package com.chanakya.repository;

import com.chanakya.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    // 🔥 By career (with resourceType)
    List<Resource> findByCareerIdAndIsActiveTrueOrderByCreatedAtDesc(Long careerId);

    // 🔥 By resource type - ab ye kaam karega
    List<Resource> findByResourceTypeAndIsActiveTrueOrderByCreatedAtDesc(String resourceType);

    // 🔥 All active resources
    List<Resource> findByIsActiveTrueOrderByCreatedAtDesc();

    // 🔥 By career and resource type
    List<Resource> findByCareerIdAndResourceTypeAndIsActiveTrueOrderByCreatedAtDesc(Long careerId, String resourceType);
}