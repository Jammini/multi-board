package study.multiproject.user.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
        super(ResponseCode.NOT_FOUND_USER);
    }

}
