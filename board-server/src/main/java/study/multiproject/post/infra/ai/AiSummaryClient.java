package study.multiproject.post.infra.ai;

public interface AiSummaryClient {

    /**
     * 게시글 내용을 요약합니다.
     *
     * @param content 요약할 내용
     * @return 요약 결과
     */
    String summarize(String content);
}
