package study.multiproject.post.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class PostNotFoundException extends BaseException {

    public PostNotFoundException() {
        super(ResponseCode.NOT_FOUND_POST);
    }
}
