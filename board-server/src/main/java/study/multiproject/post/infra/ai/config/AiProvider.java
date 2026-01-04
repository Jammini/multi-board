package study.multiproject.post.infra.ai.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import study.multiproject.post.exception.UnsupportedAiProviderException;

@Getter
@RequiredArgsConstructor
public enum AiProvider {
    CLAUDE("claude"),
    OPENAI("openai"),
    GPT("gpt"),
    GEMINI("gemini");

    private final String value;

    public static AiProvider from(String value) {
        for (AiProvider provider : values()) {
            if (provider.value.equalsIgnoreCase(value)) {
                return provider;
            }
        }
        throw new UnsupportedAiProviderException(value);
    }
}
