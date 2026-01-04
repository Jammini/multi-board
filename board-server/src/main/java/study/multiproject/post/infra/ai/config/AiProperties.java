package study.multiproject.post.infra.ai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ai")
public class AiProperties {

    private String provider;
    private ClaudeProperties claude = new ClaudeProperties();
    private OpenAiProperties openai = new OpenAiProperties();
    private GeminiProperties gemini = new GeminiProperties();

    @Getter
    @Setter
    public static class ClaudeProperties {
        private String apiKey;
        private String model = "claude-3-5-sonnet-20241022";
        private Integer maxTokens = 1024;
    }

    @Getter
    @Setter
    public static class OpenAiProperties {
        private String apiKey;
        private String model = "gpt-4o-mini";
        private Integer maxTokens = 1024;
    }

    @Getter
    @Setter
    public static class GeminiProperties {
        private String apiKey;
        private String model = "gemini-2.5-flash";
    }
}