package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "assessments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who gave assessment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Raw answers JSON (matches DB column: raw_responses)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_responses", columnDefinition = "json")
    private Map<String, Object> answers;

    // Calculated bucket scores JSON
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "bucket_scores", columnDefinition = "json")
    private Map<String, Integer> bucketScores;

    private Integer totalScore;

    private LocalDateTime completedAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        completedAt = LocalDateTime.now();
    }
}