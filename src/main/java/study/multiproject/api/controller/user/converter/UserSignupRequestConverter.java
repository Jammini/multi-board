package study.multiproject.api.controller.user.converter;

import org.springframework.stereotype.Component;
import study.multiproject.api.controller.user.request.UserSignupRequest;
import study.multiproject.api.service.user.request.UserSignupServiceRequest;

@Component
public class UserSignupRequestConverter {

    public UserSignupServiceRequest toServiceRequest(UserSignupRequest request) {
        return new UserSignupServiceRequest(request.email(), request.password(), request.name());
    }

}
