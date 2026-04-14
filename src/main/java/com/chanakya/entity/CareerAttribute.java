package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "career_attributes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"career_id", "bucket_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id", nullable = false)
    private Career career;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id", nullable = false)
    private Bucket bucket;

    @Column(nullable = false)
    private Integer weight; // 1–10

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}