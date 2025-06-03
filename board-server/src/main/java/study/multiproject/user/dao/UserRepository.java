package study.multiproject.user.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import study.multiproject.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
