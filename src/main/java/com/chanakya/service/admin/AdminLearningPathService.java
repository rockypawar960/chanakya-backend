package com.chanakya.service.admin;

import com.chanakya.entity.LearningPath;
import com.chanakya.entity.Career;
import com.chanakya.dto.admin.*;
import com.chanakya.repository.LearningPathRepository;
import com.chanakya.repository.CareerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminLearningPathService {

    private final LearningPathRepository learningPathRepository;
    private final CareerRepository careerRepository;

    /**
     * Get all learning paths with pagination
     */
    @Transactional(readOnly = true)
    public PaginatedResponse<AdminLearningPathDTO> getAllLearningPaths(Pageable pageable) {
        log.info("[AdminLearningPathService] Fetching all learning paths");
        Page<LearningPath> pathsPage = learningPathRepository.findAll(pageable);
        return PaginatedResponse.<AdminLearningPathDTO>builder()
                .content(pathsPage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(pathsPage.getNumber())
                .pageSize(pathsPage.getSize())
                .totalElements(pathsPage.getTotalElements())
                .totalPages(pathsPage.getTotalPages())
                .isLast(pathsPage.isLast())
                .build();
    }

    /**
     * Get learning path by ID
     */
    @Transactional(readOnly = true)
    public AdminLearningPathDetailsDTO getLearningPathDetails(Long pathId) {
        log.info("[AdminLearningPathService] Fetching learning path details for ID: {}", pathId);
        LearningPath path = learningPathRepository.findById(pathId)
                .orElseThrow(() -> new RuntimeException("Learning path not found with ID: " + pathId));
        return convertToDetailsDTO(path);
    }

    /**
     * Get learning paths by career
     */
    @Transactional(readOnly = true)
    public PaginatedResponse<AdminLearningPathDTO> getLearningPathsByCareer(Long careerId, Pageable pageable) {
        log.info("[AdminLearningPathService] Fetching learning paths for career ID: {}", careerId);

        // Verify career exists
        if (!careerRepository.existsById(careerId)) {
            throw new RuntimeException("Career not found with ID: " + careerId);
        }

        // ✅ This will work automatically
        Page<LearningPath> pathsPage = learningPathRepository.findByCareerId(careerId, pageable);

        return PaginatedResponse.<AdminLearningPathDTO>builder()
                .content(pathsPage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(pathsPage.getNumber())
                .pageSize(pathsPage.getSize())
                .totalElements(pathsPage.getTotalElements())
                .totalPages(pathsPage.getTotalPages())
                .isLast(pathsPage.isLast())
                .isEmpty(pathsPage.isEmpty())
                .build();
    }

    /**
     * Create new learning path
     */
    public AdminLearningPathDTO createLearningPath(CreateLearningPathRequest request) {
        log.info("[AdminLearningPathService] Creating new learning path: {}", request.getPathName());
        
        Career career = careerRepository.findById(request.getCareerId())
                .orElseThrow(() -> new RuntimeException("Career not found with ID: " + request.getCareerId()));

        LearningPath path = LearningPath.builder()
                .careerId(request.getCareerId())
                .pathName(request.getPathName())
                .description(request.getDescription())
                .durationMonths(request.getDurationMonths())
                .build();

        LearningPath savedPath = learningPathRepository.save(path);
        log.info("[AdminLearningPathService] Learning path created with ID: {}", savedPath.getId());
        return convertToDTO(savedPath);
    }

    /**
     * Update learning path
     */
    public AdminLearningPathDTO updateLearningPath(Long pathId, UpdateLearningPathRequest request) {
        log.info("[AdminLearningPathService] Updating learning path with ID: {}", pathId);
        
        LearningPath path = learningPathRepository.findById(pathId)
                .orElseThrow(() -> new RuntimeException("Learning path not found with ID: " + pathId));

        if (request.getPathName() != null) {
            path.setPathName(request.getPathName());
        }

        if (request.getDescription() != null) {
            path.setDescription(request.getDescription());
        }

        if (request.getDurationMonths() != null) {
            path.setDurationMonths(request.getDurationMonths());
        }

        LearningPath updatedPath = learningPathRepository.save(path);
        log.info("[AdminLearningPathService] Learning path updated with ID: {}", pathId);
        return convertToDTO(updatedPath);
    }

    /**
     * Delete learning path
     */
    public void deleteLearningPath(Long pathId) {
        log.info("[AdminLearningPathService] Deleting learning path with ID: {}", pathId);
        
        LearningPath path = learningPathRepository.findById(pathId)
                .orElseThrow(() -> new RuntimeException("Learning path not found with ID: " + pathId));

        learningPathRepository.delete(path);
        log.info("[AdminLearningPathService] Learning path deleted with ID: {}", pathId);
    }

    /**
     * Helper: Convert to DTO
     */
    private AdminLearningPathDTO convertToDTO(LearningPath path) {
        return AdminLearningPathDTO.builder()
                .id(path.getId())
                .careerId(path.getCareerId())
                .pathName(path.getPathName())
                .description(path.getDescription())
                .durationMonths(path.getDurationMonths())
                .stepsCount(path.getSteps() != null ? (long) path.getSteps().size() : 0L)
                .build();
    }

    /**
     * Helper: Convert to Details DTO
     */
    private AdminLearningPathDetailsDTO convertToDetailsDTO(LearningPath path) {
        Career career = careerRepository.findById(path.getCareerId()).orElse(null);
        return AdminLearningPathDetailsDTO.builder()
                .id(path.getId())
                .careerId(path.getCareerId())
                .pathName(path.getPathName())
                .description(path.getDescription())
                .durationMonths(path.getDurationMonths())
                .careerName(career != null ? career.getName() : "N/A")
                .steps(path.getSteps())
                .build();
    }
}
