package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.CareerDTO;
import com.chanakya.service.CareerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/careers")
@RequiredArgsConstructor
@Tag(name = "Career", description = "Career related endpoints")
@Slf4j
public class CareerController {

    private final CareerService careerService;

    @GetMapping
    @Operation(summary = "Get all careers", description = "Fetch all active careers sorted by popularity")
    public ResponseEntity<ApiResponse<List<CareerDTO>>> getAllCareers() {
        log.info("Fetching all careers");
        List<CareerDTO> careers = careerService.getAllCareers();

        return ResponseEntity.ok(
                ApiResponse.<List<CareerDTO>>builder()  // 🔥 IMPORTANT: Generic type specify kiya
                        .success(true)
                        .message(careers.isEmpty() ? "No careers found" : "Careers retrieved successfully")
                        .data(careers)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get career by ID", description = "Fetch a specific career by its ID")
    public ResponseEntity<ApiResponse<CareerDTO>> getCareerById(@PathVariable Long id) {
        log.info("Fetching career with id: {}", id);
        CareerDTO career = careerService.getCareerById(id);

        return ResponseEntity.ok(
                ApiResponse.<CareerDTO>builder()  // 🔥 IMPORTANT: Generic type specify kiya
                        .success(true)
                        .message("Career retrieved successfully")
                        .data(career)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get career by name", description = "Fetch a specific career by its name")
    public ResponseEntity<ApiResponse<CareerDTO>> getCareerByName(@PathVariable String name) {
        log.info("Fetching career with name: {}", name);
        CareerDTO career = careerService.getCareerByName(name);

        return ResponseEntity.ok(
                ApiResponse.<CareerDTO>builder()  // 🔥 IMPORTANT: Generic type specify kiya
                        .success(true)
                        .message("Career retrieved successfully")
                        .data(career)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}