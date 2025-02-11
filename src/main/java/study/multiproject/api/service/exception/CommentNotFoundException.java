package study.multiproject.api.service.exception;

import study.multiproject.api.error.exception.BaseException;
import study.multiproject.api.error.exception.ResponseCode;

public class CommentNotFoundException extends BaseException {

    public CommentNotFoundException() {
        super(ResponseCode.NOT_FOUND_COMMENT);
    }
}
