package study.multiproject.post.infra.ai.dto;

import java.util.List;

public record GeminiRequest(List<Content> contents) {

    public record Content(List<Part> parts) {

        public static Content text(String text) {
            return new Content(List.of(new Part(text)));
        }
    }

    public record Part(String text) {
    }
}