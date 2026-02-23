package com.chanakya.service;

import com.chanakya.dto.AssessmentRequest;
import com.chanakya.dto.QuestionDTO;
import com.chanakya.dto.QuestionOptionDTO;
import com.chanakya.entity.*;
import com.chanakya.exception.ResourceNotFoundException;
import com.chanakya.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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

    public List<QuestionDTO> getAllQuestions() {
        List<Question> questions =
                questionRepository.findByIsActiveTrueOrderBySequenceNumber();

        return questions.stream()
                .map(this::mapToQuestionDTO)
                .toList();
    }

    private QuestionDTO mapToQuestionDTO(Question question) {

        List<QuestionOptionDTO> options = question.getOptions()
                .stream()
                .map(option -> QuestionOptionDTO.builder()
                        .id(option.getId())
                        .optionText(option.getOptionText())
                        .build())
                .toList();

        return QuestionDTO.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .questionType(question.getQuestionType())
                .sequenceNumber(question.getSequenceNumber())
                .options(options)
                .build();
    }

    public Assessment submitAssessment(Long userId, AssessmentRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Map<String, Object> answers = request.getAnswers();

        // 🔥 Calculate bucket scores
        Map<String, Integer> bucketScores = calculateBucketScores(answers);

        Assessment assessment = Assessment.builder()
                .user(user)
                .answers(answers)
                .bucketScores(bucketScores)
                .totalScore(bucketScores.values().stream().mapToInt(Integer::intValue).sum())
                .completedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        Assessment saved = assessmentRepository.save(assessment);

        recommendationService.generateRecommendations(saved);

        return saved;
    }

    /**
     * Bucket scoring logic
     * request answers format:
     * {
     *   "1": 3,
     *   "2": 4
     * }
     */
    // 🔥 Get latest assessment of user
    public Assessment getLatestAssessmentByUserId(Long userId) {

        return assessmentRepository
                .findTopByUserIdAndIsActiveTrueOrderByCompletedAtDesc(userId)
                .orElse(null);
    }

    // 🔥 Get all assessments of user
    public List<Assessment> getAllAssessmentsByUserId(Long userId) {

        return assessmentRepository
                .findByUserIdAndIsActiveTrueOrderByCompletedAtDesc(userId);
    }


    private Map<String, Integer> calculateBucketScores(Map<String, Object> answers) {

        Map<String, Integer> bucketScores = new HashMap<>();

        for (Map.Entry<String, Object> entry : answers.entrySet()) {

            Long questionId = Long.parseLong(entry.getKey());

            // 🔥 SAFE score extraction
            Integer score = 0;

            if (entry.getValue() != null) {
                score = Integer.parseInt(entry.getValue().toString());
            }

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found: " + questionId));

            if (question.getBucket() == null) {
                continue;  // extra safety
            }

            String bucketName = question.getBucket().getName();

            bucketScores.put(
                    bucketName,
                    bucketScores.getOrDefault(bucketName, 0) + score
            );
        }

        return bucketScores;
    }
}