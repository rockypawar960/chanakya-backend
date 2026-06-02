package com.chanakya.service;

import com.chanakya.dto.LearningPathDTO;
import com.chanakya.dto.LearningStepDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiAgentService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String groqApiKey;

    // Groq API — free, fast, no billing needed
    private static final String GROQ_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private static final String SYSTEM_PROMPT = """
        You are a career guidance AI agent for the Chanakya platform.
        
        Your ONLY job: read the student profile and return a JSON object.
        Return ONLY raw JSON — no markdown, no ```json, no explanation, nothing else.
        
        Required JSON format:
        {
          "pathName": "Career Title Roadmap",
          "description": "One line summary of the roadmap",
          "durationMonths": 6,
          "steps": [
            {
              "stepOrder": 1,
              "level": "beginner",
              "stepName": "Step Name",
              "description": "What the student will learn",
              "videoLink": "https://www.youtube.com/results?search_query=topic+name",
              "task": "Specific measurable task"
            }
          ]
        }
        
        STRICT RULES:
        1. Return ONLY raw JSON — no ```json fence, no preamble
        2. level must be one of: beginner, intermediate, advanced
        3. Generate 5 to 8 steps in logical learning order
        4. durationMonths should match student's needs (default 6 if unclear)
        5. videoLink must be a YouTube search URL for that exact topic
        6. task must be specific and measurable
        7. stepOrder starts from 1 and increments by 1
        8. Personalize steps based on student's weak areas from reasoning
        """;

    public LearningPathDTO generateLearningPath(Long careerId, String prompt) {

        String fullPrompt = SYSTEM_PROMPT + "\n\nStudent Profile:\n" + prompt;

        // Groq uses OpenAI-compatible format
        String requestBody = """
            {
              "model": "llama-3.3-70b-versatile",
              "messages": [
                {
                  "role": "user",
                  "content": %s
                }
              ],
              "temperature": 0.3,
              "max_tokens": 2048
            }
            """.formatted(toJsonString(fullPrompt));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + groqApiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    GROQ_URL, entity, String.class);

            log.info("Groq response status: {}", response.getStatusCode());

            String groqJson = extractTextFromGroqResponse(response.getBody());
            return parseToLearningPathDTO(careerId, groqJson);

        } catch (Exception e) {
            log.error("Groq API call failed: {}", e.getMessage());
            throw new RuntimeException("Failed to generate learning path: " + e.getMessage());
        }
    }

    // Groq response structure (OpenAI format):
    // { "choices": [{ "message": { "content": "..." } }] }
    private String extractTextFromGroqResponse(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);
        String text = root
                .path("choices").get(0)
                .path("message")
                .path("content")
                .asText();

        // Clean markdown fences if model adds them
        text = text.replaceAll("```json", "").replaceAll("```", "").trim();
        log.debug("Groq raw text: {}", text);
        return text;
    }

    private LearningPathDTO parseToLearningPathDTO(Long careerId, String json) throws Exception {
        JsonNode root = objectMapper.readTree(json);

        List<LearningStepDTO> steps = new ArrayList<>();
        JsonNode stepsNode = root.path("steps");

        for (int i = 0; i < stepsNode.size(); i++) {
            JsonNode s = stepsNode.get(i);
            steps.add(LearningStepDTO.builder()
                    .id(null)
                    .learningPathId(null)
                    .stepOrder(s.path("stepOrder").asInt(i + 1))
                    .level(s.path("level").asText("beginner"))
                    .stepName(s.path("stepName").asText())
                    .description(s.path("description").asText())
                    .videoLink(s.path("videoLink").asText())
                    .task(s.path("task").asText())
                    .build()
            );
        }

        return LearningPathDTO.builder()
                .id(null)
                .careerId(careerId)
                .pathName(root.path("pathName").asText())
                .description(root.path("description").asText())
                .sequenceNumber(1)
                .durationMonths(root.path("durationMonths").asInt(6))
                .steps(steps)
                .build();
    }

    private String toJsonString(String text) {
        try {
            return objectMapper.writeValueAsString(text);
        } catch (Exception e) {
            return "\"" + text.replace("\"", "\\\"") + "\"";
        }
    }
}