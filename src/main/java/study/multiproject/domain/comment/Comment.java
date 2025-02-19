package study.multiproject.domain.comment;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    /**
     * 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 닉네임
     */
    private String nickname;

    /**
     * 댓글 내용
     */
    private String content;

    /**
     * 경로 정보
     */
    @Embedded
    private CommentPath commentPath;

    /**
     * 게시글 아이디
     */
    private Long postId;

    /**
     * 작성자 아이디
     */
    private Long writerId;

    /**
     * 삭제 여부
     */
    private boolean deleted;

    /**
     * 생성일시
     */
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Comment(Long id, String nickname, String content, CommentPath commentPath, Long postId, Long writerId) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.commentPath = commentPath;
        this.postId = postId;
        this.writerId = writerId;
        this.deleted = false;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isRoot() {
        return commentPath.isRoot();
    }

    public void delete() {
        deleted = true;
    }
}
