package study.multiproject.api.service.post.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.multiproject.domain.post.Post;

@Getter
@NoArgsConstructor
public class PostCreateServiceRequest {

    private String title;
    private String content;

    @Builder
    public PostCreateServiceRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post toEntity() {
        return Post.builder()
                   .title(title)
                   .content(content)
                   .build();
    }
}
