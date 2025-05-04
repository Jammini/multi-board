package study.multiproject.post.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class SecretPostException extends BaseException {

    public SecretPostException() {
        super(ResponseCode.SECRET_POST);
    }

}
