package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_progress",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "learning_step_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "learning_step_id", nullable = false)
    private Long learningStepId;

    @Column(nullable = false)
    private String status; // NOT_STARTED, IN_PROGRESS, COMPLETED

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    // 🔥 AUTO TIMESTAMPS
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // default status
        if (this.status == null) {
            this.status = "NOT_STARTED";
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}