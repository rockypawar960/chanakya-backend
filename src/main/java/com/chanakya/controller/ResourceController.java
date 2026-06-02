package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.ResourceDTO;
import com.chanakya.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
@Tag(name = "Resource", description = "Resource endpoints")
@Slf4j
public class ResourceController {

    private final ResourceService resourceService;

    // ─── EXISTING ────────────────────────────────────────────────────────────
    @GetMapping
    @Operation(summary = "Get all resources")
    public ResponseEntity<ApiResponse<List<ResourceDTO>>> getAllResources() {
        List<ResourceDTO> resources = resourceService.getAllResources();
        return ResponseEntity.ok(ApiResponse.<List<ResourceDTO>>builder()
                .success(true)
                .message(resources.isEmpty() ? "No resources found" : "Resources retrieved successfully")
                .data(resources)
                .status(HttpStatus.OK.value())
                .build());
    }

    // ─── EXISTING ────────────────────────────────────────────────────────────
    @GetMapping("/career/{careerId}")
    @Operation(summary = "Get resources by career ID")
    public ResponseEntity<ApiResponse<List<ResourceDTO>>> getResourcesByCareer(
            @PathVariable Long careerId) {
        List<ResourceDTO> resources = resourceService.getResourcesByCareerId(careerId);
        return ResponseEntity.ok(ApiResponse.<List<ResourceDTO>>builder()
                .success(true)
                .message(resources.isEmpty() ? "No resources found" : "Resources retrieved successfully")
                .data(resources)
                .status(HttpStatus.OK.value())
                .build());
    }

    // ─── EXISTING ────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    @Operation(summary = "Get resource by ID")
    public ResponseEntity<ApiResponse<ResourceDTO>> getResourceById(@PathVariable Long id) {
        ResourceDTO resource = resourceService.getResourceById(id);
        return ResponseEntity.ok(ApiResponse.<ResourceDTO>builder()
                .success(true)
                .message("Resource retrieved successfully")
                .data(resource)
                .status(HttpStatus.OK.value())
                .build());
    }

    // ─── NEW: AI Generate resources for a skill ───────────────────────────
    // Frontend calls this when student opens a learning step
    // Returns cached resources if already generated, else calls AI
    @PostMapping("/generate")
    @Operation(summary = "AI generate free resources for a skill",
            description = "Generates YouTube playlists, free courses, practice platforms for a skill. Returns cached if already exists.")
    public ResponseEntity<ApiResponse<List<ResourceDTO>>> generateResources(
            @RequestParam Long careerId,
            @RequestParam String skill,
            @RequestParam(defaultValue = "BEGINNER") String level,
            @RequestParam(defaultValue = "both") String language) {

        log.info("Generate resources → careerId={} skill={} level={} lang={}",
                careerId, skill, level, language);

        List<ResourceDTO> resources = resourceService
                .generateAndSaveResources(careerId, skill, level, language);

        return ResponseEntity.ok(ApiResponse.<List<ResourceDTO>>builder()
                .success(true)
                .message("Resources generated successfully for: " + skill)
                .data(resources)
                .status(HttpStatus.OK.value())
                .build());
    }

    // generate resource for searches only not for specific career
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<ResourceDTO>>> searchResources(
            @RequestParam String skill,
            @RequestParam(defaultValue = "BEGINNER") String level,
            @RequestParam(defaultValue = "both") String language) {

        List<ResourceDTO> resources =
                resourceService.generateAndSaveResources(
                        skill,
                        level,
                        language
                );

        return ResponseEntity.ok(
                ApiResponse.<List<ResourceDTO>>builder()
                        .success(true)
                        .message("Resources generated successfully")
                        .data(resources)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}