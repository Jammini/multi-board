package study.multiproject.user.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class AlreadyExistsEmailException extends BaseException {

    public AlreadyExistsEmailException() {
        super(ResponseCode.ALREADY_EXISTS_EMAIL);
    }
}
