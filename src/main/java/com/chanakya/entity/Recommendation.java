package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Version
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "career_id")
    private Career career;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    private Double matchScore;

    @Column(length = 1000)
    private String reasoning;

    private Boolean isActive;
}