package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "buckets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bucket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    // Optional: One bucket has many questions
    @OneToMany(mappedBy = "bucket")
    private List<Question> questions;

    // Optional: One bucket linked in career attributes
    @OneToMany(mappedBy = "bucket")
    private List<CareerAttribute> careerAttributes;
}