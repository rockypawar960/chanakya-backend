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
        List<Question> questions = questionRepository.findByIsActiveTrueOrderBySequenceNumber();

        // Debug: Check database se kya aa raha hai
        log.info("📥 Fetching questions from database...");
        questions.forEach(q -> {
            log.info("Question {}: {}", q.getId(), q.getQuestionText());
            q.getOptions().forEach(opt ->
                    log.info("  Option: {} = {}", opt.getOptionText(), opt.getOptionValue())
            );
        });

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
                        .optionValue(option.getOptionValue())  // ✅ YEH ADD KARO
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

    @Transactional
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

        // Ye line console mein check karo submit ke baad
        System.out.println("Received Answers: " + request.getAnswers());

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
            Integer score = 0;

            if (entry.getValue() != null) {
                score = Integer.parseInt(entry.getValue().toString());
            }

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found: " + questionId));

            if (question.getBucket() == null) continue;

            // 🔥 1. Pehle hi uppercase kar lo
            String bucketName = question.getBucket().getName().toUpperCase().trim();

            // 🔥 2. Get aur Put dono mein wahi 'bucketName' (uppercase) use karo
            bucketScores.put(
                    bucketName,
                    bucketScores.getOrDefault(bucketName, 0) + score
            );
        }
        return bucketScores;
    }
}