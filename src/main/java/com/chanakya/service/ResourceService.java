package com.chanakya.service;

import com.chanakya.dto.ResourceDTO;
import com.chanakya.entity.Career;
import com.chanakya.entity.Resource;
import com.chanakya.exception.ResourceNotFoundException;
import com.chanakya.repository.CareerRepository;
import com.chanakya.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ResourceAgentService resourceAgentService;   // ← AI agent

    // ─── EXISTING: get all resources ────────────────────────────────────────
    public List<ResourceDTO> getAllResources() {
        log.info("Fetching all active resources");
        return resourceRepository.findByIsActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── EXISTING: get by careerId ───────────────────────────────────────────
    public List<ResourceDTO> getResourcesByCareerId(Long careerId) {
        log.info("Fetching resources for careerId: {}", careerId);

        careerRepository.findById(careerId)
                .orElseThrow(() -> new ResourceNotFoundException("Career not found: " + careerId));

        return resourceRepository.findByCareerIdAndIsActiveTrueOrderByCreatedAtDesc(careerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── EXISTING: get by id ─────────────────────────────────────────────────
    public ResourceDTO getResourceById(Long id) {
        log.info("Fetching resource id: {}", id);
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
        return mapToDTO(resource);
    }

    // ─── NEW: AI generate resources for a skill + save to DB ─────────────────
    // Smart caching: if resources already exist for this career+skill → return from DB
    // Otherwise → call AI agent → save → return
    @Transactional
    public List<ResourceDTO> generateAndSaveResources(
            Long careerId,
            String skill,
            String level,
            String language) {

        log.info("Generate resources request → careerId={} skill={} level={} lang={}",
                careerId, skill, level, language);

        // Step 1: Check if resources already exist in DB for this career + skill
        List<Resource> existing = resourceRepository
                .findByCareerIdAndSkillAndIsActiveTrueOrderByCreatedAtDesc(careerId, skill);

        if (!existing.isEmpty()) {
            log.info("Returning {} cached resources for skill: {}", existing.size(), skill);
            return existing.stream().map(this::mapToDTO).collect(Collectors.toList());
        }

        // Step 2: Fetch career
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new ResourceNotFoundException("Career not found: " + careerId));

        // Step 3: Call AI agent → get List<ResourceDTO>
        List<ResourceDTO> aiResources = resourceAgentService
                .generateResources(skill, level, language, careerId);

        // Step 4: Map DTOs → entities and save
        List<Resource> entities = aiResources.stream().map(dto ->
                Resource.builder()
                        .career(career)
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .resourceType(dto.getResourceType())
                        .url(dto.getUrl())
                        .provider(dto.getProvider())
                        .difficulty(dto.getDifficulty())
                        .estimatedDuration(dto.getEstimatedDuration())
                        .language(dto.getLanguage())
                        .skill(dto.getSkill())
                        .isActive(true)
                        .build()
        ).collect(Collectors.toList());

        List<Resource> saved = resourceRepository.saveAll(entities);
        log.info("Saved {} AI resources for skill: {}", saved.size(), skill);

        // Step 5: Return as DTOs with real DB ids
        return saved.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ResourceDTO> generateAndSaveResources(
            String skill,
            String level,
            String language) {

        List<Resource> existing =
                resourceRepository
                        .findBySkillIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(skill);

        if (!existing.isEmpty()) {
            return existing.stream()
                    .map(this::mapToDTO)
                    .toList();
        }

        List<ResourceDTO> aiResources =
                resourceAgentService.generateResources(
                        skill,
                        level,
                        language,
                        null
                );

        List<Resource> entities = aiResources.stream()
                .map(dto -> Resource.builder()
                        .career(null)
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .resourceType(dto.getResourceType())
                        .url(dto.getUrl())
                        .provider(dto.getProvider())
                        .difficulty(dto.getDifficulty())
                        .estimatedDuration(dto.getEstimatedDuration())
                        .language(dto.getLanguage())
                        .skill(dto.getSkill())
                        .isActive(true)
                        .build())
                .toList();

        return resourceRepository
                .saveAll(entities)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
    // ─── DTO mapper ──────────────────────────────────────────────────────────
    private ResourceDTO mapToDTO(Resource resource) {
        Long careerId = resource.getCareer() != null ? resource.getCareer().getId() : null;
        String careerName = resource.getCareer() != null ? resource.getCareer().getName() : null;

        return ResourceDTO.builder()
                .id(resource.getId())
                .careerId(careerId)
                .careerName(careerName)
                .title(resource.getTitle())
                .description(resource.getDescription())
                .resourceType(resource.getResourceType())
                .url(resource.getUrl())
                .provider(resource.getProvider())
                .difficulty(resource.getDifficulty())
                .estimatedDuration(resource.getEstimatedDuration())
                .language(resource.getLanguage())
                .skill(resource.getSkill())
                .isActive(resource.getIsActive())
                .build();
    }
}