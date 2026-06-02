package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id", nullable = true)
    private Career career;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String resourceType;      // PLAYLIST, VIDEO, COURSE, ARTICLE, PLATFORM, ROADMAP

    @Column(nullable = false)
    private String url;

    private String provider;          // "YouTube", "Coursera", "NPTEL", "LeetCode"
    private String difficulty;        // BEGINNER, INTERMEDIATE, ADVANCED

    @Column(name = "estimated_duration")
    private String estimatedDuration; // "12 hours", "4 weeks"

    // ── NEW FIELDS ────────────────────────────────────────────────────────────
    private String language;          // "Hindi", "English", "Both"
    private String skill;             // "Java", "DSA", "Spring Boot"
    // ─────────────────────────────────────────────────────────────────────────

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}