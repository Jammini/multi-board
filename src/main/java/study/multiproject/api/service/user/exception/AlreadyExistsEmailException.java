package study.multiproject.api.service.user.exception;

import study.multiproject.api.error.exception.BaseException;
import study.multiproject.api.error.exception.ResponseCode;

public class AlreadyExistsEmailException extends BaseException {

    public AlreadyExistsEmailException() {
        super(ResponseCode.ALREADY_EXISTS_EMAIL);
    }
}
