package study.multiproject.post.infra.ai.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import study.multiproject.post.infra.ai.AiSummaryClient;
import study.multiproject.post.infra.ai.ClaudeApiClient;
import study.multiproject.post.infra.ai.GeminiApiClient;
import study.multiproject.post.infra.ai.OpenAiApiClient;

@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class AiConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public AiSummaryClient aiSummaryClient(
        AiProperties aiProperties,
        ClaudeApiClient claudeApiClient,
        OpenAiApiClient openAiApiClient,
        GeminiApiClient geminiApiClient
    ) {
        AiProvider provider = AiProvider.from(aiProperties.getProvider());
        return switch (provider) {
            case OPENAI, GPT -> openAiApiClient;
            case CLAUDE -> claudeApiClient;
            case GEMINI -> geminiApiClient;
        };
    }
}
