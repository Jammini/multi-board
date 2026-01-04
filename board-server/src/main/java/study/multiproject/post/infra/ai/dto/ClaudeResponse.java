package study.multiproject.post.infra.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ClaudeResponse(
    List<Content> content,
    @JsonProperty("stop_reason") String stopReason
) {

    public record Content(String text) {
    }
}