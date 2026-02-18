package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id", nullable = false)
    private Career career;

    @Column(nullable = false)
    private String attributeName; // e.g., "Problem Solving", "Creativity"

    @Column(nullable = false)
    private Integer weight; // Weight/importance (1-10)

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
