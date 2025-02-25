package study.multiproject.domain.comment;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.commentPath.path = :path")
    Optional<Comment> findByPath(@Param("path") String path);

    @Query(
        value = "select path from comment " +
                "where post_id = :postId and path > :pathPrefix and path like :pathPrefix% " +
                "order by path desc limit 1",
        nativeQuery = true
    )
    Optional<String> findDescendantsTopPath(
        @Param("postId") Long postId,
        @Param("pathPrefix") String pathPrefix
    );

    @Query(
        value =
            "SELECT c.id, c.nickname, c.content, c.path, c.post_id, c.writer_id, c.deleted, c.created_at "
                +
                "FROM ( " +
                "  SELECT id FROM comment " +
                "  WHERE post_id = :postId " +
                "  ORDER BY path ASC" +
                "  LIMIT :limit OFFSET :offset " +
                ") t " +
                "LEFT JOIN comment c ON t.id = c.id",
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
