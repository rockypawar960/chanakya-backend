package com.chanakya.service.admin;

import com.chanakya.entity.Career;
import com.chanakya.dto.admin.*;
import com.chanakya.repository.CareerRepository;
import com.chanakya.repository.LearningPathRepository;
import com.chanakya.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static io.jsonwebtoken.Jwts.builder;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminCareerService {

    private final CareerRepository careerRepository;
    private final LearningPathRepository learningPathRepository;
    private final ResourceRepository resourceRepository;

    /**
     * Get all careers with pagination
     */
    @Transactional(readOnly = true)
    public PaginatedResponse<AdminCareerDTO> getAllCareers(Pageable pageable) {
        log.info("[AdminCareerService] Fetching all careers with pagination");
        Page<Career> careerPage = careerRepository.findAll(pageable);

        List<AdminCareerDTO> content = careerPage.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // ✅ Method 1: Type witness
        return PaginatedResponse.<AdminCareerDTO>builder()
                .content(content)
                .pageNumber(careerPage.getNumber())
                .pageSize(careerPage.getSize())
                .totalElements(careerPage.getTotalElements())
                .totalPages(careerPage.getTotalPages())
                .isLast(careerPage.isLast())
                .isEmpty(careerPage.isEmpty())
                .build();
    }

    /**
     * Get career by ID with detailed information
     */
    @Transactional(readOnly = true)
    public AdminCareerDetailsDTO getCareerDetails(Long careerId) {
        log.info("[AdminCareerService] Fetching career details for ID: {}", careerId);
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new RuntimeException("Career not found with ID: " + careerId));
        return convertToDetailsDTO(career);
    }

    /**
     * Create new career
     */
    public AdminCareerDTO createCareer(CreateCareerRequest request) {
        log.info("[AdminCareerService] Creating new career: {}", request.getName());
        
        if (careerRepository.findByNameAndIsActiveTrue(request.getName()).isPresent()) {
            throw new RuntimeException("Career already exists with name: " + request.getName());
        }

        Career career = Career.builder()
                .name(request.getName())
                .description(request.getDescription())
                .popularityScore(request.getPopularityScore() != null ? request.getPopularityScore() : 0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        Career savedCareer = careerRepository.save(career);
        log.info("[AdminCareerService] Career created successfully with ID: {}", savedCareer.getId());
        return convertToDTO(savedCareer);
    }

    /**
     * Update existing career
     */
    public AdminCareerDTO updateCareer(Long careerId, UpdateCareerRequest request) {
        log.info("[AdminCareerService] Updating career with ID: {}", careerId);
        
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new RuntimeException("Career not found with ID: " + careerId));

        if (request.getName() != null && !request.getName().equals(career.getName())) {
            if (careerRepository.findByNameAndIsActiveTrue(request.getName()).isPresent()) {
                throw new RuntimeException("Career already exists with name: " + request.getName());
            }
            career.setName(request.getName());
        }

        if (request.getDescription() != null) {
            career.setDescription(request.getDescription());
        }

        if (request.getPopularityScore() != null) {
            career.setPopularityScore(request.getPopularityScore());
        }

        if (request.getIsActive() != null) {
            career.setIsActive(request.getIsActive());
        }

        Career updatedCareer = careerRepository.save(career);
        log.info("[AdminCareerService] Career updated successfully with ID: {}", careerId);
        return convertToDTO(updatedCareer);
    }

    /**
     * Delete career
     */
    public void deleteCareer(Long careerId) {
        log.info("[AdminCareerService] Deleting career with ID: {}", careerId);
        
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new RuntimeException("Career not found with ID: " + careerId));

        careerRepository.delete(career);
        log.info("[AdminCareerService] Career deleted successfully with ID: {}", careerId);
    }

    /**
     * Helper: Convert Career to DTO
     */
    private AdminCareerDTO convertToDTO(Career career) {
        return AdminCareerDTO.builder()
                .id(career.getId())
                .name(career.getName())
                .description(career.getDescription())
                .popularityScore(career.getPopularityScore())
                .isActive(career.getIsActive())
                .createdAt(career.getCreatedAt())
                .updatedAt(career.getUpdatedAt())
                .build();
    }

    /**
     * Helper: Convert Career to Details DTO
     */
    private AdminCareerDetailsDTO convertToDetailsDTO(Career career) {
        long learningPathsCount = learningPathRepository.findByCareerId(career.getId()).size();
        long resourcesCount = resourceRepository.findByCareerIdAndIsActiveTrueOrderByCreatedAtDesc(career.getId()).size();

        return AdminCareerDetailsDTO.builder()
                .id(career.getId())
                .name(career.getName())
                .description(career.getDescription())
                .popularityScore(career.getPopularityScore())
                .isActive(career.getIsActive())
                .associatedLearningPathsCount(learningPathsCount)
                .associatedResourcesCount(resourcesCount)
                .createdAt(career.getCreatedAt())
                .updatedAt(career.getUpdatedAt())
                .build();
    }
}
