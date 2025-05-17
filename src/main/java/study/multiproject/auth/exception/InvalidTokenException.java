package study.multiproject.auth.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class InvalidTokenException extends BaseException {

    public InvalidTokenException() {
        super(ResponseCode.INVALID_TOKEN);
    }
}
