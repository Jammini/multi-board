package study.multiproject.api.service.post.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditServiceRequest {

    private final String title;
    private final String content;

    @Builder
    public PostEditServiceRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
