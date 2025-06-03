package study.multiproject.comment.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.multiproject.comment.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.commentPath.path = :path")
    Optional<Comment> findByPath(@Param("path") String path);

    @Query(
        value = """
                select path from comment 
                where post_id = :postId and path > :pathPrefix and path like :pathPrefix% 
                order by path desc limit 1
                """,
        nativeQuery = true
    )
    Optional<String> findDescendantsTopPath(
        @Param("postId") Long postId,
        @Param("pathPrefix") String pathPrefix
    );

    @Query(
        value = """
               select c from Comment c
               left join fetch c.user u
               where c.postId = :postId
               order by c.commentPath.path asc, c.id asc
               """
    )
    List<Comment> findAll(
        @Param("postId") Long postId,
        Pageable pageable
    );

    @Query(
        value = """
                select count(*) from (
                    select id from comment where post_id = :postId
                ) t
                """,
        nativeQuery = true
    )
    Long count(
        @Param("postId") Long postId
    );
}
