package study.multiproject.api.service.post.exception;

import study.multiproject.api.error.exception.BaseException;
import study.multiproject.api.error.exception.ErrorCode;

public class PostNotFoundException extends BaseException {

    public PostNotFoundException() {
        super(ErrorCode.NOT_FOUND_POST);
    }
}
