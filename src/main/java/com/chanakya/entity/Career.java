package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "careers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requiredSkills;

    @Column(columnDefinition = "TEXT")
    private String jobScope;

    @Column(columnDefinition = "TEXT")
    private String salaryRange;

    @Column(columnDefinition = "TEXT")
    private String educationPath;

    @Column(columnDefinition = "TEXT")
    private String topCompanies;

    @Column(name = "popularity_score")
    private Integer popularityScore;

    // 🔥 IMPORTANT: Link to CareerAttribute
    @OneToMany(mappedBy = "career", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CareerAttribute> careerAttributes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

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