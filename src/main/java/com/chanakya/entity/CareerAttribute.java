package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "career_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Career link
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id", nullable = false)
    private Career career;

    // Bucket link
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id", nullable = false)
    private Bucket bucket;

    // Importance weight (1–10)
    @Column(nullable = false)
    private Integer weight;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}