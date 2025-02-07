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
import study.multiproject.domain.comment.Comment;
import study.multiproject.domain.comment.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * 댓글 생성
     */
    @Transactional
    public CommentResponse create(CommentCreateServiceRequest request) {
        Comment parent = findParentComment(request.parentCommentId());
        Comment comment = commentRepository.save(request.toEntity(parent));
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
            request.pageable().getOffset(),
            request.pageable().getPageSize()
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
            commentRepository.findById(comment.getParentCommentId())
                .filter(Comment::isDeleted)
                .filter(not(this::hasChildren))
                .ifPresent(this::recursionDelete);
        }
    }

    /**
     * 댓글의 자식 댓글이 있는지 확인
     */
    private boolean hasChildren(Comment comment) {
        return commentRepository.countBy(comment.getPostId(), comment.getId(), 2L) == 2;
    }

    /**
     * 부모 댓글 조회
     */
    private Comment findParentComment(Long parentCommentId) {
        if (parentCommentId == null) {
            return null;
        }
        return commentRepository.findById(parentCommentId)
                   .filter(not(Comment::isDeleted))
                   .filter(Comment::isRoot)
                   .orElseThrow(ParentCommentNotFoundException::new);
    }
}
