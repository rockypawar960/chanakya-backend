package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "learning_paths")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "career_id")
    private Long careerId;

    private String pathName;
    private String description;
    private Integer durationMonths;

    @OneToMany(mappedBy = "learningPath", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<LearningStep> steps;
    // getters & setters
}