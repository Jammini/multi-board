package study.multiproject.global.config.security;

import java.util.List;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class UserPrincipal extends User {

    private final Long userId;
    private final String name;

    public UserPrincipal(study.multiproject.user.domain.User user) {
        super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.userId = user.getId();
        this.name = user.getName();
    }

}
