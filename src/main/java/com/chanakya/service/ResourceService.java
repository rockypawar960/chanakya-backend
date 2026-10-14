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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final CareerRepository careerRepository;
    private final ResourceAgentService resourceAgentService;   // AI agent

    // ─── get all resources ───────────────────────────────────────────────────
    public List<ResourceDTO> getAllResources() {
        log.info("Fetching all active resources");
        return resourceRepository.findByIsActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── get by careerId ─────────────────────────────────────────────────────
    public List<ResourceDTO> getResourcesByCareerId(Long careerId) {
        log.info("Fetching resources for careerId: {}", careerId);

        careerRepository.findById(careerId)
                .orElseThrow(() -> new ResourceNotFoundException("Career not found: " + careerId));

        return resourceRepository.findByCareerIdAndIsActiveTrueOrderByCreatedAtDesc(careerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── get by id ───────────────────────────────────────────────────────────
    public ResourceDTO getResourceById(Long id) {
        log.info("Fetching resource id: {}", id);
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
        return mapToDTO(resource);
    }

    // ─── AI generate resources for a career + skill, save to DB ──────────────
    // Cache: resources pehle se hain to DB se return, nahi to AI call → save → return
    public List<ResourceDTO> generateAndSaveResources(
            Long careerId,
            String skill,
            String level,
            String language) {

        skill = normalizeSkill(skill);

        log.info("Generate resources request → careerId={} skill={} level={} lang={}",
                careerId, skill, level, language);

        // Step 1: cache check
        List<Resource> existing = resourceRepository
                .findByCareerIdAndSkillAndIsActiveTrueOrderByCreatedAtDesc(careerId, skill);

        if (!existing.isEmpty()) {
            log.info("Returning {} cached resources for skill: {}", existing.size(), skill);
            return existing.stream().map(this::mapToDTO).collect(Collectors.toList());
        }

        // Step 2: career fetch
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new ResourceNotFoundException("Career not found: " + careerId));

        // Step 3: AI call
        List<ResourceDTO> aiResources = resourceAgentService
                .generateResources(skill, level, language, careerId);

        // Step 4: dedupe + map + save
        List<Resource> entities = toEntities(aiResources, career, skill);

        List<Resource> saved = resourceRepository.saveAll(entities);
        log.info("Saved {} AI resources for skill: {}", saved.size(), skill);

        return saved.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // ─── AI generate resources for a skill (bina career) ─────────────────────
    public List<ResourceDTO> generateAndSaveResources(
            String skill,
            String level,
            String language) {

        skill = normalizeSkill(skill);

        List<Resource> existing = resourceRepository
                .findBySkillIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(skill);

        if (!existing.isEmpty()) {
            return existing.stream().map(this::mapToDTO).toList();
        }

        List<ResourceDTO> aiResources =
                resourceAgentService.generateResources(skill, level, language, null);

        List<Resource> entities = toEntities(aiResources, null, skill);

        return resourceRepository.saveAll(entities)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private String normalizeSkill(String skill) {
        if (skill == null || skill.isBlank()) {
            throw new IllegalArgumentException("Skill is required");
        }
        return skill.trim();
    }

    /**
     * AI response ko entities me badalta hai.
     * - empty/null response pe error
     * - same URL dobara aaye to skip (duplicate se bachne ke liye)
     */
    private List<Resource> toEntities(List<ResourceDTO> aiResources, Career career, String skill) {

        if (aiResources == null || aiResources.isEmpty()) {
            throw new RuntimeException("AI did not return any resources for skill: " + skill);
        }

        Set<String> seenUrls = new HashSet<>();

        List<Resource> entities = aiResources.stream()
                .filter(dto -> dto.getUrl() != null
                        && !dto.getUrl().isBlank()
                        && seenUrls.add(dto.getUrl().trim()))
                .map(dto -> Resource.builder()
                        .career(career)
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .resourceType(dto.getResourceType())
                        .url(dto.getUrl().trim())
                        .provider(dto.getProvider())
                        .difficulty(dto.getDifficulty())
                        .estimatedDuration(dto.getEstimatedDuration())
                        .language(dto.getLanguage())
                        .skill(dto.getSkill())
                        .isActive(true)
                        .build())
                .toList();

        if (entities.isEmpty()) {
            throw new RuntimeException("AI returned no valid resources for skill: " + skill);
        }

        return entities;
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