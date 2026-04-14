package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "learning_steps") // ✅ FIXED
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String level;
    private String stepName;

    @Column(length = 1000)
    private String description;

    private String videoLink;
    private String task;
    private Integer stepOrder;

    @ManyToOne
    @JoinColumn(name = "learning_path_id")
    @JsonBackReference
    private LearningPath learningPath;
}