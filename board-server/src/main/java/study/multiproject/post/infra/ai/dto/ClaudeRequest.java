package study.multiproject.post.infra.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ClaudeRequest(
    String model,
    @JsonProperty("max_tokens") Integer maxTokens,
    List<Message> messages
) {

    public record Message(String role, String content) {

        public static Message user(String content) {
            return new Message("user", content);
        }
    }
}