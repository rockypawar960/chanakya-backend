package com.chanakya.repository;

import com.chanakya.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    // ── EXISTING ─────────────────────────────────────────────────────────────

    Page<Resource> findByCareerId(Long careerId, Pageable pageable);
    Page<Resource> findByResourceType(String resourceType, Pageable pageable);
    Page<Resource> findByCareerIdAndResourceType(Long careerId, String resourceType, Pageable pageable);
    Page<Resource> findByCareerIdAndIsActiveTrue(Long careerId, Pageable pageable);

    List<Resource> findByCareerIdAndIsActiveTrueOrderByCreatedAtDesc(Long careerId);
    List<Resource> findByResourceTypeAndIsActiveTrueOrderByCreatedAtDesc(String resourceType);
    List<Resource> findByIsActiveTrueOrderByCreatedAtDesc();
    List<Resource> findByCareerIdAndResourceTypeAndIsActiveTrueOrderByCreatedAtDesc(Long careerId, String resourceType);

    boolean existsByCareerId(Long careerId);
    long countByCareerId(Long careerId);

    // ── NEW: for skill-based caching ─────────────────────────────────────────
    // Used to check if resources already exist before calling AI again
    List<Resource> findByCareerIdAndSkillAndIsActiveTrueOrderByCreatedAtDesc(Long careerId, String skill);

    // Filter by language
    List<Resource> findByCareerIdAndLanguageAndIsActiveTrueOrderByCreatedAtDesc(Long careerId, String language);

    // Filter by skill only (cross-career)
    List<Resource> findBySkillAndIsActiveTrueOrderByCreatedAtDesc(String skill);

    List<Resource> findBySkillIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(
            String skill
    );

//    // Total resources the user has opened / viewed
//    @Query("SELECT COUNT(rv) FROM ResourceView rv WHERE rv.user.id = :userId")
//    int countViewedByUserId(Long userId);
//
//    // Resources the user has bookmarked
//    @Query("SELECT COUNT(b) FROM Bookmark b WHERE b.user.id = :userId")
//    int countBookmarkedByUserId(Long userId);
}