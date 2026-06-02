package com.chanakya.service;

import com.chanakya.dto.ResourceDTO;
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
public class ResourceAgentService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String groqApiKey;

    private static final String GROQ_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    // AI generates metadata + searchQuery ONLY — never URLs
    private static final String SYSTEM_PROMPT = """
        You are a free learning resource curator for the Chanakya career guidance platform.
        
        Return ONLY a raw JSON array — no markdown, no ```json, no explanation, nothing else.
        
        Required JSON format:
        [
          {
            "title": "Resource title",
            "description": "What the student will learn from this resource in 1-2 sentences",
            "resourceType": "PLAYLIST",
            "searchQuery": "best search query to find this resource",
            "provider": "YouTube",
            "difficulty": "BEGINNER",
            "estimatedDuration": "15 hours",
            "language": "Hindi",
            "skill": "Java"
          }
        ]
        
        RESOURCE TYPES (pick exactly one):
        - PLAYLIST      -> YouTube playlist series
        - VIDEO         -> Single long YouTube video (freeCodeCamp style)
        - COURSE        -> Free course on any platform
        - PRACTICE      -> Coding practice platform
        - DOCUMENTATION -> Official docs / guides
        - ROADMAP       -> Structured learning guide
        
        FREE YOUTUBE PROVIDERS:
        - "YouTube" (Hindi channels: CodeWithHarry, Apna College, Kunal Kushwaha, Shradha Khapra)
        - "YouTube" (English channels: freeCodeCamp, Traversy Media, The Net Ninja, Telusko, Amigoscode)
        
        FREE COURSE PLATFORMS:
        - "Coursera"             -> Free audit available
        - "NPTEL"                -> Free IIT/IISc courses
        - "edX"                  -> Free audit available
        - "MIT OpenCourseWare"   -> Completely free MIT courses
        
        FREE PRACTICE PLATFORMS:
        - "LeetCode"
        - "HackerRank"
        - "GeeksForGeeks"
        
        FREE CORPORATE LEARNING PORTALS (REPUTED COMPANIES):
        - "IBM SkillsBuild"      -> Free AI, Cloud, Cybersecurity, Data Science courses by IBM
        - "Google"               -> Free Digital Marketing, AI, Cloud, Android courses by Google
        - "Microsoft Learn"      -> Free Azure, AI, .NET, Python, Power BI courses by Microsoft
        - "AWS Skill Builder"    -> Free Cloud, DevOps, ML, Serverless courses by Amazon
        - "Cisco NetAcad"        -> Free Networking, Cybersecurity, Python, IoT courses by Cisco
        - "Infosys Springboard"  -> Free Java, Python, Full Stack, Digital courses by Infosys
        - "TCS iON"              -> Free Programming, Data Science, Soft Skills by TCS
        - "Meta Blueprint"       -> Free Social Media, Marketing, AR courses by Meta
        - "roadmap.sh"           -> Free structured learning roadmaps
        - "Official Docs"        -> Official documentation
        
        WHEN TO RECOMMEND CORPORATE PROVIDERS:
        - Cloud / DevOps        -> AWS Skill Builder, Microsoft Learn, Google
        - Networking / Security -> Cisco NetAcad, IBM SkillsBuild
        - Java / Full Stack     -> Infosys Springboard, TCS iON
        - AI / ML / Data        -> IBM SkillsBuild, Google, Microsoft Learn, AWS Skill Builder
        - Python                -> Cisco NetAcad, Infosys Springboard, Microsoft Learn
        - Android / Mobile      -> Google
        - Marketing / Social    -> Meta Blueprint, Google
        - General Programming   -> Infosys Springboard, TCS iON, NPTEL
        
        STRICT RULES:
        1. Return ONLY raw JSON array — no ```json fence, no preamble, no extra text
        2. Generate exactly 8 resources
        3. NEVER generate any URL — only fill "searchQuery" field
        4. searchQuery = the best search term to find this exact resource
        5. For Hindi YouTube: "java tutorial hindi codewithharry 2024"
        6. For English YouTube: "java full course freeCodeCamp english"
        7. For corporate: "java programming infosys springboard free"
        8. Always include BOTH Hindi AND English resources if language is "both"
        9. Always include at least 1 corporate provider (IBM/Google/Microsoft/AWS/Cisco/Infosys/TCS)
        10. Always include: 2 YouTube, 1 free course, 1 corporate, 1 practice, 1 docs/roadmap
        11. difficulty must be one of: BEGINNER, INTERMEDIATE, ADVANCED
        12. language must be one of: Hindi, English, Both
        """;

    // Build REAL URLs from provider name — AI never touches URLs
    private String buildRealUrl(String provider, String searchQuery, String skill) {
        String encodedQuery = searchQuery.trim().replace(" ", "+");
        String encodedSkill = skill.trim().replace(" ", "+");
        String slugSkill    = skill.toLowerCase().replace(" ", "-");

        return switch (provider.toLowerCase().trim()) {

            // ── YouTube ──────────────────────────────────────────────────────
            case "youtube" ->
                    "https://www.youtube.com/results?search_query=" + encodedQuery;

            // ── Free Course Platforms ─────────────────────────────────────────
            case "coursera" ->
                    "https://www.coursera.org/search?query=" + encodedSkill + "&price=free";

            case "nptel" ->
                    "https://nptel.ac.in/course.html#searchresult?q=" + encodedSkill;

            case "edx" ->
                    "https://www.edx.org/search?q=" + encodedSkill + "&price=Free";

            case "mit opencourseware" ->
                    "https://ocw.mit.edu/search/?q=" + encodedSkill;

            // ── Practice Platforms ────────────────────────────────────────────
            case "leetcode" ->
                    "https://leetcode.com/problemset/?search=" + encodedSkill;

            case "hackerrank" ->
                    "https://www.hackerrank.com/domains/" + slugSkill;

            case "geeksforgeeks" ->
                    "https://www.geeksforgeeks.org/" + slugSkill + "/";

            // ── Roadmap / Docs ────────────────────────────────────────────────
            case "roadmap.sh" ->
                    "https://roadmap.sh/" + slugSkill;

            case "official docs" ->
                    "https://www.google.com/search?q=" + encodedSkill + "+official+documentation";

            // ── Corporate Free Learning Portals ───────────────────────────────
            case "ibm skillsbuild" ->
                    "https://skillsbuild.org/students/course-catalog?search=" + encodedSkill;

            case "google" ->
                    "https://grow.google/intl/en_in/?q=" + encodedSkill;

            case "microsoft learn" ->
                    "https://learn.microsoft.com/en-us/training/browse/?terms=" + encodedSkill;

            case "aws skill builder" ->
                    "https://explore.skillbuilder.aws/learn/catalog?searchText=" + encodedSkill;

            case "cisco netacad" ->
                    "https://www.netacad.com/courses?courseLang=en-US&search=" + encodedSkill;

            case "infosys springboard" ->
                    "https://infyspringboard.onwingspan.com/web/en/search?q=" + encodedSkill;

            case "tcs ion" ->
                    "https://learning.tcsionhub.in/hub/search?q=" + encodedSkill;

            case "meta blueprint" ->
                    "https://www.facebook.com/business/learn/courses?q=" + encodedSkill;

            // ── Fallback ──────────────────────────────────────────────────────
            default ->
                    "https://www.google.com/search?q=" + encodedQuery + "+free+course";
        };
    }

    // Main method called by ResourceService
    public List<ResourceDTO> generateResources(String skill, String level, String language, Long careerId) {

        String userPrompt = String.format(
                "Generate the BEST free learning resources for:\n" +
                        "Skill: %s\nLevel: %s\nLanguage preference: %s\n\n" +
                        "Include top YouTube playlists (Hindi + English), free courses, " +
                        "at least one reputed company portal (IBM/Google/Microsoft/AWS/Cisco/Infosys/TCS), " +
                        "and a practice platform.",
                skill, level, language);

        String requestBody;
        try {
            String fullPrompt = SYSTEM_PROMPT + "\n\n" + userPrompt;
            requestBody = String.format(
                    "{\n" +
                            "  \"model\": \"llama-3.3-70b-versatile\",\n" +
                            "  \"messages\": [{\"role\": \"user\", \"content\": %s}],\n" +
                            "  \"temperature\": 0.2,\n" +
                            "  \"max_tokens\": 3000\n" +
                            "}",
                    objectMapper.writeValueAsString(fullPrompt)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to build request: " + e.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + groqApiKey);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    GROQ_URL, entity, String.class);

            log.info("Groq response status: {}", response.getStatusCode());

            String rawJson = extractTextFromGroqResponse(response.getBody());
            return parseToResourceDTOs(rawJson, skill, careerId);

        } catch (Exception e) {
            log.error("Groq resource agent failed: {}", e.getMessage());
            throw new RuntimeException("Failed to generate resources: " + e.getMessage());
        }
    }

    // Extract text content from Groq OpenAI-format response
    private String extractTextFromGroqResponse(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);
        String text = root
                .path("choices").get(0)
                .path("message")
                .path("content")
                .asText();

        text = text.replaceAll("```json", "").replaceAll("```", "").trim();
        log.debug("Groq raw text: {}", text);
        return text;
    }

    // Parse JSON array -> List<ResourceDTO> — URLs always built by us, never AI
    private List<ResourceDTO> parseToResourceDTOs(String json, String skill, Long careerId) throws Exception {
        JsonNode root = objectMapper.readTree(json);

        List<ResourceDTO> resources = new ArrayList<>();

        for (JsonNode r : root) {
            String provider     = r.path("provider").asText("YouTube");
            String searchQuery  = r.path("searchQuery").asText(skill + " tutorial free");
            String resourceType = r.path("resourceType").asText("VIDEO");

            // Always build real URL — never use AI-generated URL
            String realUrl = buildRealUrl(provider, searchQuery, skill);

            resources.add(ResourceDTO.builder()
                    .id(null)
                    .careerId(careerId)
                    .title(r.path("title").asText())
                    .description(r.path("description").asText())
                    .resourceType(resourceType)
                    .url(realUrl)                      // 100% real, always works
                    .provider(provider)
                    .difficulty(r.path("difficulty").asText("BEGINNER"))
                    .estimatedDuration(r.path("estimatedDuration").asText("Self-paced"))
                    .language(r.path("language").asText("English"))
                    .skill(skill)
                    .isActive(true)
                    .build()
            );
        }

        log.info("Generated {} resources with real URLs for skill: {}", resources.size(), skill);
        return resources;
    }
}