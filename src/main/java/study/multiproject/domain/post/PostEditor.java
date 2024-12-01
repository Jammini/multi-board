package study.multiproject.domain.post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {

    private final String title;
    private final String content;

    @Builder
    protected PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
