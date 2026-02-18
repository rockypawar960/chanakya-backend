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

    @GetMapping
    @Operation(summary = "Get all resources", description = "Fetch all active resources")
    public ResponseEntity<ApiResponse<List<ResourceDTO>>> getAllResources() {
        log.info("Fetching all resources");
        List<ResourceDTO> resources = resourceService.getAllResources();

        return ResponseEntity.ok(
                ApiResponse.<List<ResourceDTO>>builder()  // 🔥 FIX: Generic type specify
                        .success(true)
                        .message(resources.isEmpty() ? "No resources found" : "Resources retrieved successfully")
                        .data(resources)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/career/{careerId}")
    @Operation(summary = "Get resources for career", description = "Get all resources for a specific career")
    public ResponseEntity<ApiResponse<List<ResourceDTO>>> getResourcesByCareer(
            @PathVariable Long careerId) {
        log.info("Fetching resources for career id: {}", careerId);
        List<ResourceDTO> resources = resourceService.getResourcesByCareerId(careerId);  // 🔥 Method name sahi kiya

        return ResponseEntity.ok(
                ApiResponse.<List<ResourceDTO>>builder()  // 🔥 FIX: Generic type specify
                        .success(true)
                        .message(resources.isEmpty() ? "No resources found" : "Resources retrieved successfully")
                        .data(resources)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get resource by ID", description = "Fetch a specific resource by its ID")
    public ResponseEntity<ApiResponse<ResourceDTO>> getResourceById(@PathVariable Long id) {
        log.info("Fetching resource with id: {}", id);
        ResourceDTO resource = resourceService.getResourceById(id);

        return ResponseEntity.ok(
                ApiResponse.<ResourceDTO>builder()  // 🔥 FIX: Generic type specify
                        .success(true)
                        .message("Resource retrieved successfully")
                        .data(resource)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}