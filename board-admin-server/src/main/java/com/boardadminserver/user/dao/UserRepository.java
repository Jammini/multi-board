package com.boardadminserver.user.dao;

import com.boardadminserver.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT u FROM User u
        WHERE (:keyword IS NULL OR
              LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
              LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
              LOWER(u.nickname) LIKE LOWER(CONCAT('%', :keyword, '%')))
        ORDER BY u.createdAt DESC
    """)
    List<User> searchByKeyword(@Param("keyword") String keyword);
}
