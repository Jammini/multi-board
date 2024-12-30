package study.multiproject.api.service.post.request;

import java.util.List;

public record PostEditServiceRequest(String title, String content, List<String> hashtags) {
}
