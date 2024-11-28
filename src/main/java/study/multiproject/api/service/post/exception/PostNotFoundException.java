package study.multiproject.api.service.post.exception;

import study.multiproject.api.error.exception.BaseException;
import study.multiproject.api.error.exception.ResponseCode;

public class PostNotFoundException extends BaseException {

    public PostNotFoundException() {
        super(ResponseCode.NOT_FOUND_POST);
    }
}
