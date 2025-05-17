package study.multiproject.auth.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class PasswordMismatchException extends BaseException {

    public PasswordMismatchException() {
        super(ResponseCode.PASSWORD_MISMATCH);
    }
}
