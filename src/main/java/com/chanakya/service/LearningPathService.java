package com.chanakya.service;

import com.chanakya.dto.LearningPathDTO;
import com.chanakya.entity.Career;
import com.chanakya.entity.LearningPath;
import com.chanakya.exception.ResourceNotFoundException;
import com.chanakya.repository.CareerRepository;
import com.chanakya.repository.LearningPathRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LearningPathService {

    private final LearningPathRepository learningPathRepository;
    private final CareerRepository careerRepository;
    private final ModelMapper modelMapper;

    public List<LearningPathDTO> getLearningPathsByCareer(Long careerId) {
        // Verify career exists
        careerRepository.findById(careerId)
                .orElseThrow(() -> new ResourceNotFoundException("Career not found with id: " + careerId));

        List<LearningPath> paths = learningPathRepository
                .findByCareerIdAndIsActiveTrueOrderBySequenceNumber(careerId);

        return paths.stream()
                .map(path -> {
                    LearningPathDTO dto = modelMapper.map(path, LearningPathDTO.class);
                    dto.setCareerId(careerId);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public LearningPathDTO getLearningPathById(Long id) {
        LearningPath path = learningPathRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Learning path not found with id: " + id));
        LearningPathDTO dto = modelMapper.map(path, LearningPathDTO.class);
        dto.setCareerId(path.getCareer().getId());
        return dto;
    }
}
