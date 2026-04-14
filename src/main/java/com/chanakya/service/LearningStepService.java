package com.chanakya.service;

import com.chanakya.dto.LearningStepDTO;
import com.chanakya.entity.LearningStep;
import com.chanakya.repository.LearningStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LearningStepService {

    private final LearningStepRepository stepRepo;

    public List<LearningStepDTO> getStepsByPath(Long pathId) {

        List<LearningStep> steps =
                stepRepo.findByLearningPathIdOrderByStepOrder(pathId);

        return steps.stream().map(step ->
                LearningStepDTO.builder()
                        .id(step.getId())
                        .learningPathId(step.getLearningPath().getId()) // 🔥 important
                        .level(step.getLevel())
                        .stepName(step.getStepName())
                        .description(step.getDescription())
                        .videoLink(step.getVideoLink())
                        .task(step.getTask())
                        .stepOrder(step.getStepOrder())
                        .build()
        ).toList();
    }
}