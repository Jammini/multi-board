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
import study.multiproject.post.infra.ai.dto.GeminiRequest;
import study.multiproject.post.infra.ai.dto.GeminiResponse;

@Slf4j
@Component
public class GeminiApiClient implements AiSummaryClient {
    private static final String GEMINI_API_URL_TEMPLATE = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent";
    private static final String HEADER_GOOG_API_KEY = "x-goog-api-key";

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String model;

    public GeminiApiClient(
        RestTemplate restTemplate,
        AiProperties aiProperties
    ) {
        this.restTemplate = restTemplate;
        this.apiKey = aiProperties.getGemini().getApiKey();
        this.model = aiProperties.getGemini().getModel();
    }

    @Override
    public String summarize(String content) {
        try {
            GeminiRequest request = new GeminiRequest(
                List.of(GeminiRequest.Content.text(
                    "다음 게시글을 3-5문장으로 요약해주세요. 핵심 내용만 간결하게 정리해주세요:\n\n" + content
                ))
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HEADER_GOOG_API_KEY, apiKey);

            HttpEntity<GeminiRequest> httpEntity = new HttpEntity<>(request, headers);

            String url = String.format(GEMINI_API_URL_TEMPLATE, model);

            GeminiResponse response = restTemplate.postForObject(
                url,
                httpEntity,
                GeminiResponse.class
            );

            if (response == null
                || response.candidates() == null
                || response.candidates().isEmpty()
                || response.candidates().get(0).content() == null
                || response.candidates().get(0).content().parts() == null
                || response.candidates().get(0).content().parts().isEmpty()) {
                return "요약을 생성할 수 없습니다.";
            }

            return response.candidates().get(0).content().parts().get(0).text();

        } catch (Exception e) {
            throw new AiSummaryException(e.getMessage());
        }
    }
}
