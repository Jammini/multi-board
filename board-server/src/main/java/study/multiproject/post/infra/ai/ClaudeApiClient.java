package study.multiproject.post.infra.ai;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import study.multiproject.post.exception.AiSummaryException;
import study.multiproject.post.infra.ai.config.AiProperties;
import study.multiproject.post.infra.ai.dto.ClaudeRequest;
import study.multiproject.post.infra.ai.dto.ClaudeResponse;

@Slf4j
@Component
public class ClaudeApiClient implements AiSummaryClient {

    private static final String CLAUDE_API_URL = "https://api.anthropic.com/v1/messages";
    private static final String ANTHROPIC_VERSION = "2023-06-01";
    private static final String HEADER_API_KEY = "x-api-key";
    private static final String HEADER_ANTHROPIC_VERSION = "anthropic-version";

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String model;
    private final Integer maxTokens;

    public ClaudeApiClient(
        RestTemplate restTemplate,
        AiProperties aiProperties
    ) {
        this.restTemplate = restTemplate;
        this.apiKey = aiProperties.getClaude().getApiKey();
        this.model = aiProperties.getClaude().getModel();
        this.maxTokens = aiProperties.getClaude().getMaxTokens();
    }

    @Override
    public String summarize(String content) {
        try {
            ClaudeRequest request = new ClaudeRequest(
                model,
                maxTokens,
                List.of(ClaudeRequest.Message.user(
                    "다음 게시글을 3-5문장으로 요약해주세요. 핵심 내용만 간결하게 정리해주세요:\n\n" + content
                ))
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HEADER_API_KEY, apiKey);
            headers.set(HEADER_ANTHROPIC_VERSION, ANTHROPIC_VERSION);

            HttpEntity<ClaudeRequest> httpEntity = new HttpEntity<>(request, headers);

            ClaudeResponse response = restTemplate.postForObject(
                CLAUDE_API_URL,
                httpEntity,
                ClaudeResponse.class
            );

            if (response == null || response.content() == null || response.content().isEmpty()) {
                return "요약을 생성할 수 없습니다.";
            }

            return response.content().get(0).text();

        } catch (Exception e) {
            throw new AiSummaryException(e.getMessage());
        }
    }
}
