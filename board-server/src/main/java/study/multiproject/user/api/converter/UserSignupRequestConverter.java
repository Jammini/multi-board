package study.multiproject.user.api.converter;

import org.springframework.stereotype.Component;
import study.multiproject.user.api.request.UserSignupRequest;
import study.multiproject.user.application.request.UserSignupServiceRequest;

@Component
public class UserSignupRequestConverter {

    public UserSignupServiceRequest toServiceRequest(UserSignupRequest request) {
        return new UserSignupServiceRequest(request.email(), request.password(), request.name());
    }

}
