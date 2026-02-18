package com.chanakya.service;

import com.chanakya.dto.AssessmentRequest;
import com.chanakya.dto.QuestionDTO;
import com.chanakya.entity.Assessment;
import com.chanakya.entity.Question;
import com.chanakya.entity.User;
import com.chanakya.exception.ResourceNotFoundException;
import com.chanakya.repository.AssessmentRepository;
import com.chanakya.repository.QuestionRepository;
import com.chanakya.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AssessmentService {

    private final QuestionRepository questionRepository;
    private final AssessmentRepository assessmentRepository;
    private final UserRepository userRepository;
    private final RecommendationService recommendationService;

    /**
     * Get all active questions
     */
    public List<QuestionDTO> getAllQuestions() {
        log.info("Fetching all active questions");

        List<Question> questions = questionRepository.findByIsActiveTrueOrderBySequenceNumber();

        return questions.stream()
                .map(this::mapToQuestionDTO)
                .collect(Collectors.toList());
    }

    /**
     * Submit assessment
     */
    public Assessment submitAssessment(Long userId, AssessmentRequest request) {
        log.info("Submitting assessment for user id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Assessment assessment = Assessment.builder()
                .user(user)
                .answers(request.getAnswers())
                .totalScore(calculateScore(request))
                .completedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        Assessment savedAssessment = assessmentRepository.save(assessment);
        log.info("Assessment saved with id: {}", savedAssessment.getId());

        // Generate recommendations based on assessment
        recommendationService.generateRecommendations(savedAssessment);

        return savedAssessment;
    }

    /**
     * Get latest assessment by user ID
     */
    public Assessment getLatestAssessmentByUserId(Long userId) {
        log.info("Fetching latest assessment for user id: {}", userId);

        return assessmentRepository.findTopByUserIdAndIsActiveTrueOrderByCompletedAtDesc(userId)
                .orElse(null);
    }

    /**
     * Get all assessments by user ID
     */
    public List<Assessment> getAllAssessmentsByUserId(Long userId) {
        log.info("Fetching all assessments for user id: {}", userId);

        return assessmentRepository.findByUserIdAndIsActiveTrueOrderByCompletedAtDesc(userId);
    }

    /**
     * Calculate score based on answers
     */
    private Integer calculateScore(AssessmentRequest request) {
        // TODO: Implement scoring logic
        return 100;
    }

    /**
     * Map Question entity to QuestionDTO
     */
    private QuestionDTO mapToQuestionDTO(Question question) {
        return QuestionDTO.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .questionType(question.getQuestionType())
                .sequenceNumber(question.getSequenceNumber())
                .build();
    }
}