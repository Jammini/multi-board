package com.boardbatchserver.post.dao;

import com.boardbatchserver.post.domain.Post;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT MAX(p.id) FROM Post p WHERE p.createdAt < :end")
    Long findMaxPostIdBefore(@Param("end") LocalDateTime end);
}
