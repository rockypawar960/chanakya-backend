package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_step_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStepProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long stepId;

    private Boolean completed;

    private LocalDateTime completedAt;
}
