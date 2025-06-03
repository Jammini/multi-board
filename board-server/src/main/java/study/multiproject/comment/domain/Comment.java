package study.multiproject.comment.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import study.multiproject.user.domain.User;

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
     * 삭제 여부
     */
    private boolean deleted;

    /**
     * 생성일시
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 작성자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Comment(Long id, String nickname, String content, CommentPath commentPath, Long postId, User user) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.commentPath = commentPath;
        this.postId = postId;
        this.user = user;
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
