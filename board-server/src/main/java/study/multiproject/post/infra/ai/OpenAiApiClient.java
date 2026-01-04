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
import study.multiproject.post.infra.ai.dto.OpenAiRequest;
import study.multiproject.post.infra.ai.dto.OpenAiResponse;

@Slf4j
@Component
public class OpenAiApiClient implements AiSummaryClient {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String model;
    private final Integer maxTokens;

    public OpenAiApiClient(
        RestTemplate restTemplate,
        AiProperties aiProperties
    ) {
        this.restTemplate = restTemplate;
        this.apiKey = aiProperties.getOpenai().getApiKey();
        this.model = aiProperties.getOpenai().getModel();
        this.maxTokens = aiProperties.getOpenai().getMaxTokens();
    }

    @Override
    public String summarize(String content) {
        try {
            OpenAiRequest request = new OpenAiRequest(
                model,
                maxTokens,
                List.of(OpenAiRequest.Message.user(
                    "다음 게시글을 3-5문장으로 요약해주세요. 핵심 내용만 간결하게 정리해주세요:\n\n" + content
                ))
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<OpenAiRequest> httpEntity = new HttpEntity<>(request, headers);

            OpenAiResponse response = restTemplate.postForObject(
                OPENAI_API_URL,
                httpEntity,
                OpenAiResponse.class
            );

            if (response == null || response.choices() == null || response.choices().isEmpty()) {
                return "요약을 생성할 수 없습니다.";
            }

            return response.choices().get(0).message().content();

        } catch (Exception e) {
            throw new AiSummaryException(e.getMessage());
        }
    }
}
