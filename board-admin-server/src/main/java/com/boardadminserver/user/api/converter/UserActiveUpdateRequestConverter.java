package com.boardadminserver.user.api.converter;

import com.boardadminserver.user.api.request.UserActiveUpdateRequest;
import com.boardadminserver.user.application.request.UserActiveUpdateServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class UserActiveUpdateRequestConverter {

    public UserActiveUpdateServiceRequest toServiceRequest(Long userId, UserActiveUpdateRequest request) {
        return new UserActiveUpdateServiceRequest(
            userId,
            request.isActive()
        );
    }
}
