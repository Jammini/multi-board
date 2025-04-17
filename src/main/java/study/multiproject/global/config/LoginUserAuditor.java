package study.multiproject.global.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import study.multiproject.global.config.security.UserPrincipal;

public class LoginUserAuditor implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UserPrincipal userPrincipal) {
            return Optional.of(userPrincipal.getName());
        }
        return Optional.empty();
    }
}
