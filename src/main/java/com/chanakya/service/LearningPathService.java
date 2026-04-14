package com.chanakya.service;

import com.chanakya.dto.LearningPathDTO;
import com.chanakya.dto.LearningStepDTO;
import com.chanakya.entity.LearningPath;
import com.chanakya.repository.LearningPathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LearningPathService {

    private final LearningPathRepository pathRepo;

    public LearningPathDTO getPathWithSteps(Long pathId) {

        LearningPath path = pathRepo.findById(pathId)
                .orElseThrow(() -> new RuntimeException("Path not found"));

        return convertToDTO(path);
    }

    private LearningPathDTO convertToDTO(LearningPath path) {

        return LearningPathDTO.builder()
                .id(path.getId())
                .careerId(path.getCareerId())
                .pathName(path.getPathName())
                .description(path.getDescription())
                .durationMonths(path.getDurationMonths())

                // 🔥 Steps mapping
                .steps(path.getSteps().stream().map(step ->
                        LearningStepDTO.builder()
                                .id(step.getId())
                                .learningPathId(path.getId())
                                .level(step.getLevel())
                                .stepName(step.getStepName())
                                .description(step.getDescription())
                                .videoLink(step.getVideoLink())
                                .task(step.getTask())
                                .stepOrder(step.getStepOrder())
                                .build()
                ).toList())

                .build();
    }
}