package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_learning_paths",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "learning_path_id"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLearningPath {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_path_id", nullable = false)
    private LearningPath learningPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LearningPathStatus status;

    @Column(name = "progress_percentage", nullable = false)
    private Integer progressPercentage = 0;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }

        if (lastAccessedAt == null) {
            lastAccessedAt = LocalDateTime.now();
        }

        if (status == null) {
            status = LearningPathStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void setCurrent(boolean b) {
    }

    public static enum LearningPathStatus {

        ACTIVE,
        COMPLETED,
        PAUSED,
        DROPPED

    }

}
