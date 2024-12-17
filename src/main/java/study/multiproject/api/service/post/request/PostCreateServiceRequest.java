package study.multiproject.api.service.post.request;

import study.multiproject.domain.post.Post;

public record PostCreateServiceRequest(String title, String content) {

    public Post toEntity() {
        return Post.builder()
                   .title(title)
                   .content(content)
                   .build();
    }
}
