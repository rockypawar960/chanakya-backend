package com.chanakya.service.admin;

import com.chanakya.entity.Resource;
import com.chanakya.entity.Career;
import com.chanakya.dto.admin.*;
import com.chanakya.repository.ResourceRepository;
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
public class AdminResourceService {

    private final ResourceRepository resourceRepository;
    private final CareerRepository careerRepository;

    @Transactional(readOnly = true)
    public PaginatedResponse<AdminResourceDTO> getAllResources(Pageable pageable) {
        log.info("[AdminResourceService] Fetching all resources");
        Page<Resource> resourcePage = resourceRepository.findAll(pageable);

        return PaginatedResponse.<AdminResourceDTO>builder()
                .content(resourcePage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(resourcePage.getNumber())
                .pageSize(resourcePage.getSize())
                .totalElements(resourcePage.getTotalElements())
                .totalPages(resourcePage.getTotalPages())
                .isLast(resourcePage.isLast())
                .isEmpty(resourcePage.isEmpty())
                .build();
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<AdminResourceDTO> getResourcesByCareer(Long careerId, Pageable pageable) {
        log.info("[AdminResourceService] Fetching resources for career ID: {}", careerId);

        // Verify career exists
        if (!careerRepository.existsById(careerId)) {
            throw new RuntimeException("Career not found with ID: " + careerId);
        }

        // ✅ Using paginated method
        Page<Resource> resourcePage = resourceRepository.findByCareerId(careerId, pageable);

        return PaginatedResponse.<AdminResourceDTO>builder()
                .content(resourcePage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(resourcePage.getNumber())
                .pageSize(resourcePage.getSize())
                .totalElements(resourcePage.getTotalElements())
                .totalPages(resourcePage.getTotalPages())
                .isLast(resourcePage.isLast())
                .isEmpty(resourcePage.isEmpty())
                .build();
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<AdminResourceDTO> getResourcesByType(String resourceType, Pageable pageable) {
        log.info("[AdminResourceService] Fetching resources by type: {}", resourceType);

        // ✅ Using paginated method
        Page<Resource> resourcePage = resourceRepository.findByResourceType(resourceType, pageable);

        return PaginatedResponse.<AdminResourceDTO>builder()
                .content(resourcePage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(resourcePage.getNumber())
                .pageSize(resourcePage.getSize())
                .totalElements(resourcePage.getTotalElements())
                .totalPages(resourcePage.getTotalPages())
                .isLast(resourcePage.isLast())
                .isEmpty(resourcePage.isEmpty())
                .build();
    }

    // Optional: Get resources by career and type combined
    @Transactional(readOnly = true)
    public PaginatedResponse<AdminResourceDTO> getResourcesByCareerAndType(Long careerId, String resourceType, Pageable pageable) {
        log.info("[AdminResourceService] Fetching resources for career ID: {} and type: {}", careerId, resourceType);

        if (!careerRepository.existsById(careerId)) {
            throw new RuntimeException("Career not found with ID: " + careerId);
        }

        Page<Resource> resourcePage = resourceRepository.findByCareerIdAndResourceType(careerId, resourceType, pageable);

        return PaginatedResponse.<AdminResourceDTO>builder()
                .content(resourcePage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(resourcePage.getNumber())
                .pageSize(resourcePage.getSize())
                .totalElements(resourcePage.getTotalElements())
                .totalPages(resourcePage.getTotalPages())
                .isLast(resourcePage.isLast())
                .isEmpty(resourcePage.isEmpty())
                .build();
    }

    @Transactional(readOnly = true)
    public AdminResourceDTO getResourceById(Long resourceId) {
        log.info("[AdminResourceService] Fetching resource by ID: {}", resourceId);
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + resourceId));
        return convertToDTO(resource);
    }

    public AdminResourceDTO createResource(CreateResourceRequest request) {
        log.info("[AdminResourceService] Creating new resource: {}", request.getTitle());

        Career career = careerRepository.findById(request.getCareerId())
                .orElseThrow(() -> new RuntimeException("Career not found with ID: " + request.getCareerId()));

        Resource resource = Resource.builder()
                .career(career)
                .title(request.getTitle())
                .description(request.getDescription())
                .resourceType(request.getResourceType())
                .url(request.getUrl())
                .provider(request.getProvider())
                .difficulty(request.getDifficulty())
                .estimatedDuration(request.getEstimatedDuration())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        Resource savedResource = resourceRepository.save(resource);
        log.info("[AdminResourceService] Resource created with ID: {}", savedResource.getId());
        return convertToDTO(savedResource);
    }

    public AdminResourceDTO updateResource(Long resourceId, UpdateResourceRequest request) {
        log.info("[AdminResourceService] Updating resource ID: {}", resourceId);

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + resourceId));

        if (request.getTitle() != null) resource.setTitle(request.getTitle());
        if (request.getDescription() != null) resource.setDescription(request.getDescription());
        if (request.getResourceType() != null) resource.setResourceType(request.getResourceType());
        if (request.getUrl() != null) resource.setUrl(request.getUrl());
        if (request.getProvider() != null) resource.setProvider(request.getProvider());
        if (request.getDifficulty() != null) resource.setDifficulty(request.getDifficulty());
        if (request.getEstimatedDuration() != null) resource.setEstimatedDuration(request.getEstimatedDuration());
        if (request.getIsActive() != null) resource.setIsActive(request.getIsActive());

        Resource updatedResource = resourceRepository.save(resource);
        log.info("[AdminResourceService] Resource updated with ID: {}", resourceId);
        return convertToDTO(updatedResource);
    }

    public void deleteResource(Long resourceId) {
        log.info("[AdminResourceService] Deleting resource ID: {}", resourceId);

        if (!resourceRepository.existsById(resourceId)) {
            throw new RuntimeException("Resource not found with ID: " + resourceId);
        }

        resourceRepository.deleteById(resourceId);
        log.info("[AdminResourceService] Resource deleted with ID: {}", resourceId);
    }

    private AdminResourceDTO convertToDTO(Resource resource) {
        return AdminResourceDTO.builder()
                .id(resource.getId())
                .careerId(resource.getCareer() != null ? resource.getCareer().getId() : null)
                .careerName(resource.getCareer() != null ? resource.getCareer().getName() : "N/A")
                .title(resource.getTitle())
                .description(resource.getDescription())
                .resourceType(resource.getResourceType())
                .url(resource.getUrl())
                .provider(resource.getProvider())
                .difficulty(resource.getDifficulty())
                .estimatedDuration(resource.getEstimatedDuration())
                .isActive(resource.getIsActive())
                .createdAt(resource.getCreatedAt())
                .updatedAt(resource.getUpdatedAt())
                .build();
    }
}