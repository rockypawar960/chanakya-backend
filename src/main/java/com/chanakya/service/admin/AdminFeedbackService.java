package com.chanakya.service.admin;

import com.chanakya.entity.Feedback;
import com.chanakya.entity.User;
import com.chanakya.dto.admin.*;
import com.chanakya.repository.FeedbackRepository;
import com.chanakya.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminFeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    /**
     * Get all feedback with pagination
     */
    @Transactional(readOnly = true)
    public PaginatedResponse<AdminFeedbackDTO> getAllFeedback(Pageable pageable) {
        log.info("[AdminFeedbackService] Fetching all feedback");
        Page<Feedback> feedbackPage = feedbackRepository.findAll(pageable);
        return PaginatedResponse.<AdminFeedbackDTO>builder()
                .content(feedbackPage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(feedbackPage.getNumber())
                .pageSize(feedbackPage.getSize())
                .totalElements(feedbackPage.getTotalElements())
                .totalPages(feedbackPage.getTotalPages())
                .isLast(feedbackPage.isLast())
                .build();
    }

    /**
     * Get feedback by status
     */
    @Transactional(readOnly = true)
    public PaginatedResponse<AdminFeedbackDTO> getFeedbackByStatus(String status, Pageable pageable) {
        log.info("[AdminFeedbackService] Fetching feedback by status: {}", status);
        Page<Feedback> feedbackPage = feedbackRepository.findByStatus(status, pageable);
        return PaginatedResponse.<AdminFeedbackDTO>builder()
                .content(feedbackPage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(feedbackPage.getNumber())
                .pageSize(feedbackPage.getSize())
                .totalElements(feedbackPage.getTotalElements())
                .totalPages(feedbackPage.getTotalPages())
                .isLast(feedbackPage.isLast())
                .build();
    }

    /**
     * Get feedback by type
     */
    @Transactional(readOnly = true)
    public PaginatedResponse<AdminFeedbackDTO> getFeedbackByType(String feedbackType, Pageable pageable) {
        log.info("[AdminFeedbackService] Fetching feedback by type: {}", feedbackType);
        Page<Feedback> feedbackPage = feedbackRepository.findByFeedbackType(feedbackType, pageable);
        return PaginatedResponse.<AdminFeedbackDTO>builder()
                .content(feedbackPage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(feedbackPage.getNumber())
                .pageSize(feedbackPage.getSize())
                .totalElements(feedbackPage.getTotalElements())
                .totalPages(feedbackPage.getTotalPages())
                .isLast(feedbackPage.isLast())
                .build();
    }

    /**
     * Get feedback by user ID
     */
    @Transactional(readOnly = true)
    public PaginatedResponse<AdminFeedbackDTO> getFeedbackByUser(Long userId, Pageable pageable) {
        log.info("[AdminFeedbackService] Fetching feedback for user ID: {}", userId);
        Page<Feedback> feedbackPage = feedbackRepository.findByUserId(userId, pageable);
        return PaginatedResponse.<AdminFeedbackDTO>builder()
                .content(feedbackPage.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .pageNumber(feedbackPage.getNumber())
                .pageSize(feedbackPage.getSize())
                .totalElements(feedbackPage.getTotalElements())
                .totalPages(feedbackPage.getTotalPages())
                .isLast(feedbackPage.isLast())
                .build();
    }

    /**
     * Get feedback by ID
     */
    @Transactional(readOnly = true)
    public AdminFeedbackDTO getFeedbackById(Long feedbackId) {
        log.info("[AdminFeedbackService] Fetching feedback ID: {}", feedbackId);
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + feedbackId));
        return convertToDTO(feedback);
    }

    /**
     * Create feedback (from user)
     */
    public AdminFeedbackDTO createFeedback(Long userId, CreateFeedbackRequest request) {
        log.info("[AdminFeedbackService] Creating feedback for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Feedback feedback = Feedback.builder()
                .user(user)
                .subject(request.getSubject())
                .message(request.getMessage())
                .feedbackType(request.getFeedbackType())
                .rating(request.getRating())
                .status("OPEN")
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);
        log.info("[AdminFeedbackService] Feedback created with ID: {}", savedFeedback.getId());
        return convertToDTO(savedFeedback);
    }

    /**
     * Update feedback response and status (admin)
     */
    public AdminFeedbackDTO updateFeedbackResponse(Long feedbackId, UpdateFeedbackResponseRequest request) {
        log.info("[AdminFeedbackService] Updating feedback response for ID: {}", feedbackId);
        
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + feedbackId));

        feedback.setAdminResponse(request.getAdminResponse());
        feedback.setStatus(request.getStatus());
        
        if ("RESOLVED".equals(request.getStatus())) {
            feedback.setResolvedAt(LocalDateTime.now());
        }

        Feedback updatedFeedback = feedbackRepository.save(feedback);
        log.info("[AdminFeedbackService] Feedback updated with ID: {}", feedbackId);
        return convertToDTO(updatedFeedback);
    }

    /**
     * Delete feedback
     */
    public void deleteFeedback(Long feedbackId) {
        log.info("[AdminFeedbackService] Deleting feedback ID: {}", feedbackId);
        
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + feedbackId));

        feedbackRepository.delete(feedback);
        log.info("[AdminFeedbackService] Feedback deleted with ID: {}", feedbackId);
    }

    /**
     * Get feedback statistics
     */
    @Transactional(readOnly = true)
    public FeedbackStatisticsDTO getFeedbackStatistics() {
        log.info("[AdminFeedbackService] Calculating feedback statistics");
        
        long totalFeedback = feedbackRepository.count();
        long openCount = feedbackRepository.countByStatus("OPEN");
        long inProgressCount = feedbackRepository.countByStatus("IN_PROGRESS");
        long resolvedCount = feedbackRepository.countByStatus("RESOLVED");
        long closedCount = feedbackRepository.countByStatus("CLOSED");

        return FeedbackStatisticsDTO.builder()
                .totalFeedback(totalFeedback)
                .openCount(openCount)
                .inProgressCount(inProgressCount)
                .resolvedCount(resolvedCount)
                .closedCount(closedCount)
                .resolutionRate(totalFeedback > 0 ? (resolvedCount * 100.0 / totalFeedback) : 0.0)
                .build();
    }

    /**
     * Helper: Convert to DTO
     */
    private AdminFeedbackDTO convertToDTO(Feedback feedback) {
        return AdminFeedbackDTO.builder()
                .id(feedback.getId())
                .userId(feedback.getUser().getId())
                .userEmail(feedback.getUser().getEmail())
                .subject(feedback.getSubject())
                .message(feedback.getMessage())
                .feedbackType(feedback.getFeedbackType())
                .status(feedback.getStatus())
                .rating(feedback.getRating())
                .adminResponse(feedback.getAdminResponse())
                .createdAt(feedback.getCreatedAt())
                .updatedAt(feedback.getUpdatedAt())
                .resolvedAt(feedback.getResolvedAt())
                .build();
    }
}
