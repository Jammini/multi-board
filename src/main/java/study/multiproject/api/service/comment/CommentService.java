package study.multiproject.api.service.comment;

import static java.util.function.Predicate.not;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.api.service.comment.request.CommentCreateServiceRequest;
import study.multiproject.api.service.comment.request.CommentPageServiceRequest;
import study.multiproject.api.service.comment.response.CommentPageResponse;
import study.multiproject.api.service.comment.response.CommentResponse;
import study.multiproject.api.service.exception.CommentNotFoundException;
import study.multiproject.api.service.exception.ParentCommentNotFoundException;
import study.multiproject.api.service.user.UserService;
import study.multiproject.domain.comment.Comment;
import study.multiproject.domain.comment.CommentPath;
import study.multiproject.domain.comment.CommentRepository;
import study.multiproject.domain.user.User;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;

    /**
     * 댓글 생성
     */
    @Transactional
    public CommentResponse create(CommentCreateServiceRequest request) {
        Comment parent = findParentComment(request.path());
        CommentPath parentCommentPath = parent == null ? CommentPath.create("") : parent.getCommentPath();

        // 부모 댓글의 자식 댓글 중 TopPath를 찾는다.
        String descendantsTopPath = commentRepository.findDescendantsTopPath(request.postId(),
            parentCommentPath.getPath()).orElse(null);

        // 자식 댓글의 Path를 생성한다.
        CommentPath childCommentPath = parentCommentPath.createChildCommentPath(descendantsTopPath);

        User user = userService.getUserById(request.writerId());
        Comment comment = commentRepository.save(request.toEntity(childCommentPath, user));
        return new CommentResponse(comment);
    }

    /**
     * 댓글 조회
     */
    public CommentResponse read(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                              .orElseThrow(CommentNotFoundException::new);
        return new CommentResponse(comment);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void delete(Long commentId) {
        commentRepository.findById(commentId)
            .filter(not(Comment::isDeleted))
            .ifPresent(comment -> {
                if (hasChildren(comment)) { // 자식 댓글이 있는 경우 삭제 처리
                    comment.delete();
                } else { // 자식 댓글이 없는 경우 재귀적 삭제
                    recursionDelete(comment);
                }
            });
    }

    /**
     * 댓글 목록 조회
     */
    public CommentPageResponse readAll(CommentPageServiceRequest request) {
        List<Comment> comments = commentRepository.findAll(
            request.postId(),
            request.pageable()
        );

        List<CommentResponse> commentResponses = comments.stream()
                                                     .map(CommentResponse::new)
                                                     .toList();
        Long totalComments = commentRepository.count(request.postId());
        return new CommentPageResponse(commentResponses, totalComments);
    }

    /**
     * 댓글 재귀적 삭제
     */
    private void recursionDelete(Comment comment) {
        commentRepository.delete(comment);
        if (!comment.isRoot()) {
            commentRepository.findByPath(comment.getCommentPath().getParentPath())
                .filter(Comment::isDeleted)
                .filter(not(this::hasChildren))
                .ifPresent(this::recursionDelete);
        }
    }

    /**
     * 댓글의 자식 댓글이 있는지 확인
     */
    private boolean hasChildren(Comment comment) {
        return commentRepository.findDescendantsTopPath(
            comment.getPostId(),
            comment.getCommentPath().getPath()
        ).isPresent();
    }

    /**
     * 부모 댓글 조회
     */
    private Comment findParentComment(String parentPath) {
        if (parentPath == null) {
            return null;
        }
        return commentRepository.findByPath(parentPath)
                   .filter(not(Comment::isDeleted))
                   .orElseThrow(ParentCommentNotFoundException::new);
    }
}
