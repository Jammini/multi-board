package study.multiproject.domain.comment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(
        value = "select count(*) from (" +
                    " select id from comment " +
                    " where post_id = :postId and parent_comment_id = :parentCommentId " +
                    " limit :limit" +
                    ") t",
        nativeQuery = true
    )
    Long countBy(
        @Param("postId") Long postId,
        @Param("parentCommentId") Long parentCommentId,
        @Param("limit") Long limit
    );

    @Query(
        value =
            "SELECT c.id, c.nickname, c.content, c.parent_comment_id, c.post_id, c.writer_id, c.deleted, c.created_at "
                +
                "FROM ( " +
                "  SELECT id FROM comment " +
                "  WHERE post_id = :postId " +
                "  ORDER BY parent_comment_id ASC, id ASC " +
                "  LIMIT :limit OFFSET :offset " +
                ") t " +
                "JOIN comment c ON t.id = c.id",
        nativeQuery = true
    )
    List<Comment> findAll(
        @Param("postId") Long postId,
        @Param("offset") Long offset,
        @Param("limit") int limit
    );

    @Query(
        value = "select count(*) from (" +
                    " select id from comment where post_id = :postId " +
                    ") t",
        nativeQuery = true
    )
    Long count(
        @Param("postId") Long postId
    );
}
