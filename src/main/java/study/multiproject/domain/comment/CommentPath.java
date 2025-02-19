package study.multiproject.domain.comment;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.multiproject.domain.comment.exception.ChunkOverflowException;
import study.multiproject.domain.comment.exception.DepthOverflowException;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentPath {
    private String path;

    private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyz";

    private static final int DEPTH_CHUNK_SIZE = 5;
    private static final int MAX_DEPTH = 5;

    private static final String MIN_CHUNK = String.valueOf(CHARSET.charAt(0)).repeat(DEPTH_CHUNK_SIZE); // "00000"
    private static final String MAX_CHUNK = String.valueOf(CHARSET.charAt(CHARSET.length() - 1)).repeat(DEPTH_CHUNK_SIZE); // "zzzzz"

    /**
     * 경로 생성
     */
    public static CommentPath create(String path) {
        if (isDepthOverflowed(path)) {
            throw new DepthOverflowException();
        }

        CommentPath commentPath = new CommentPath();
        commentPath.path = path;
        return commentPath;
    }

    /**
     * depth가 최대값인지 확인
     */
    private static boolean isDepthOverflowed(String path) {
        return calDepth(path) > MAX_DEPTH;
    }

    /**
     * depth 계산
     */
    private static int calDepth(String path) {
        return path.length() / DEPTH_CHUNK_SIZE;
    }

    /**
     * depth 반환
     */
    public int getDepth() {
        return calDepth(path);
    }

    /**
     * 루트 댓글인지 확인
     */
    public boolean isRoot() {
        return calDepth(path) == 1;
    }

    /**
     * 부모 chunk 반환
     */
    public String getParentPath() {
        return path.substring(0, path.length() - DEPTH_CHUNK_SIZE);
    }

    /**
     * 자식 댓글 경로 생성
     */
    public CommentPath createChildCommentPath(String descendantsTopPath) {
        if (descendantsTopPath == null) {
            return CommentPath.create(path + MIN_CHUNK);
        }
        String childrenTopPath = findChildrenTopPath(descendantsTopPath);
        return CommentPath.create(increase(childrenTopPath));
    }

    /**
     * 자식 댓글 경로 생성을 위한 부모 경로 찾기
     */
    private String findChildrenTopPath(String descendantsTopPath) {
        return descendantsTopPath.substring(0, (getDepth() + 1) * DEPTH_CHUNK_SIZE);
    }

    /**
     * 경로 증가
     */
    private String increase(String path) {
        // 끝에 있는 chunk를 잘라낸다.
        String lastChunk = path.substring(path.length() - DEPTH_CHUNK_SIZE);
        if (isChunkOverflowed(lastChunk)) {
            throw new ChunkOverflowException();
        }

        int charsetLength = CHARSET.length();
        int value = 0;
        for (char ch : lastChunk.toCharArray()) {
            value = value * charsetLength + CHARSET.indexOf(ch);
        }
        value = value + 1;

        String result = "";
        for(int i = 0; i < DEPTH_CHUNK_SIZE; i++) {
            result = CHARSET.charAt(value % charsetLength) + result;
            value = value / charsetLength;
        }
        return path.substring(0, path.length() - DEPTH_CHUNK_SIZE) + result;
    }

    /**
     * chunk가 최대값인지 확인
     */
    private boolean isChunkOverflowed(String chunk) {
        return MAX_CHUNK.equals(chunk);
    }
}
