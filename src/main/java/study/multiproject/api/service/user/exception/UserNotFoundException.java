package study.multiproject.api.service.user.exception;

import study.multiproject.api.error.exception.BaseException;
import study.multiproject.api.error.exception.ResponseCode;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
        super(ResponseCode.NOT_FOUND_USER);
    }

}
