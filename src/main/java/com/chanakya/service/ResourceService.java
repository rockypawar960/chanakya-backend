package com.chanakya.service;

import com.chanakya.dto.ResourceDTO;
import com.chanakya.entity.Career;
import com.chanakya.entity.Resource;
import com.chanakya.exception.ResourceNotFoundException;
import com.chanakya.repository.CareerRepository;
import com.chanakya.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final CareerRepository careerRepository;
    private final ModelMapper modelMapper;

    public List<ResourceDTO> getResourcesByCareerId(Long careerId) {
        log.info("Fetching resources for career id: {}", careerId);

        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new ResourceNotFoundException("Career not found with id: " + careerId));

        List<Resource> resources = resourceRepository
                .findByCareerIdAndIsActiveTrueOrderByCreatedAtDesc(careerId);

        return resources.stream()
                .map(resource -> mapToDTO(resource, careerId))
                .collect(Collectors.toList());
    }

    public ResourceDTO getResourceById(Long id) {
        log.info("Fetching resource with id: {}", id);

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        Long careerId = resource.getCareer() != null ? resource.getCareer().getId() : null;
        return mapToDTO(resource, careerId);
    }

    public List<ResourceDTO> getAllResources() {
        log.info("Fetching all active resources");

        List<Resource> resources = resourceRepository.findByIsActiveTrueOrderByCreatedAtDesc();

        return resources.stream()
                .map(resource -> {
                    Long careerId = resource.getCareer() != null ? resource.getCareer().getId() : null;
                    return mapToDTO(resource, careerId);
                })
                .collect(Collectors.toList());
    }

    /**
     * Map Resource entity to ResourceDTO
     */
    private ResourceDTO mapToDTO(Resource resource, Long careerId) {
        // Map using ModelMapper
        ResourceDTO dto = modelMapper.map(resource, ResourceDTO.class);

        // Set careerId
        dto.setCareerId(careerId);

        // Set career name if career exists
        if (careerId != null) {
            careerRepository.findById(careerId).ifPresent(career -> {
                dto.setCareerName(career.getName());  // 🔥 Ab ye kaam karega
            });
        }

        // Ensure isActive is set
        if (dto.getIsActive() == null) {
            dto.setIsActive(resource.getIsActive());
        }

        return dto;
    }
}