package com.chanakya.service.admin;

import com.chanakya.entity.RecommendationWeightConfig;
import com.chanakya.dto.admin.*;
import com.chanakya.repository.RecommendationWeightConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminRecommendationConfigService {

    private final RecommendationWeightConfigRepository configRepository;

    /**
     * Get all recommendation configs
     */
    @Transactional(readOnly = true)
    public PaginatedResponse<AdminRecommendationConfigDTO> getAllConfigs(Pageable pageable) {
        log.info("[AdminRecommendationConfigService] Fetching all recommendation configs");
        Page<RecommendationWeightConfig> configPage = configRepository.findAll(pageable);
        return PaginatedResponse.<AdminRecommendationConfigDTO>builder()
                .content(configPage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(configPage.getNumber())
                .pageSize(configPage.getSize())
                .totalElements(configPage.getTotalElements())
                .totalPages(configPage.getTotalPages())
                .isLast(configPage.isLast())
                .build();
    }

    /**
     * Get active configs only
     */
    @Transactional(readOnly = true)
    public List<AdminRecommendationConfigDTO> getActiveConfigs() {
        log.info("[AdminRecommendationConfigService] Fetching active configs");
        return configRepository.findByIsActiveTrueOrderByBucketName()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get config by ID
     */
    @Transactional(readOnly = true)
    public AdminRecommendationConfigDTO getConfigById(Long configId) {
        log.info("[AdminRecommendationConfigService] Fetching config ID: {}", configId);
        RecommendationWeightConfig config = configRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("Config not found with ID: " + configId));
        return convertToDTO(config);
    }

    /**
     * Get config by bucket name
     */
    @Transactional(readOnly = true)
    public AdminRecommendationConfigDTO getConfigByBucketName(String bucketName) {
        log.info("[AdminRecommendationConfigService] Fetching config for bucket: {}", bucketName);
        RecommendationWeightConfig config = configRepository.findByBucketName(bucketName)
                .orElseThrow(() -> new RuntimeException("Config not found for bucket: " + bucketName));
        return convertToDTO(config);
    }

    /**
     * Create new config
     */
    public AdminRecommendationConfigDTO createConfig(CreateRecommendationConfigRequest request) {
        log.info("[AdminRecommendationConfigService] Creating config for bucket: {}", request.getBucketName());

        if (configRepository.findByBucketName(request.getBucketName()).isPresent()) {
            throw new RuntimeException("Config already exists for bucket: " + request.getBucketName());
        }

        RecommendationWeightConfig config = RecommendationWeightConfig.builder()
                .bucketName(request.getBucketName())
                .weight(request.getWeight())
                .thresholdScore(request.getThresholdScore())
                .description(request.getDescription())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        RecommendationWeightConfig savedConfig = configRepository.save(config);
        log.info("[AdminRecommendationConfigService] Config created with ID: {}", savedConfig.getId());
        return convertToDTO(savedConfig);
    }

    /**
     * Update config
     */
    public AdminRecommendationConfigDTO updateConfig(Long configId, UpdateRecommendationConfigRequest request) {
        log.info("[AdminRecommendationConfigService] Updating config ID: {}", configId);

        RecommendationWeightConfig config = configRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("Config not found with ID: " + configId));

        if (request.getWeight() != null) {
            config.setWeight(request.getWeight());
        }

        if (request.getThresholdScore() != null) {
            config.setThresholdScore(request.getThresholdScore());
        }

        if (request.getDescription() != null) {
            config.setDescription(request.getDescription());
        }

        if (request.getIsActive() != null) {
            config.setIsActive(request.getIsActive());
        }

        RecommendationWeightConfig updatedConfig = configRepository.save(config);
        log.info("[AdminRecommendationConfigService] Config updated with ID: {}", configId);
        return convertToDTO(updatedConfig);
    }

    /**
     * Delete config
     */
    public void deleteConfig(Long configId) {
        log.info("[AdminRecommendationConfigService] Deleting config ID: {}", configId);

        RecommendationWeightConfig config = configRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("Config not found with ID: " + configId));

        configRepository.delete(config);
        log.info("[AdminRecommendationConfigService] Config deleted with ID: {}", configId);
    }

    /**
     * Helper: Convert to DTO
     */
    private AdminRecommendationConfigDTO convertToDTO(RecommendationWeightConfig config) {
        return AdminRecommendationConfigDTO.builder()
                .id(config.getId())
                .bucketName(config.getBucketName())
                .weight(config.getWeight())
                .thresholdScore(config.getThresholdScore())
                .isActive(config.getIsActive())
                .description(config.getDescription())
                .createdAt(config.getCreatedAt())
                .updatedAt(config.getUpdatedAt())
                .build();
    }
}
