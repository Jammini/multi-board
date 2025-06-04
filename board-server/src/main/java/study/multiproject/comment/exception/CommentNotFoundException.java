package study.multiproject.comment.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class CommentNotFoundException extends BaseException {

    public CommentNotFoundException() {
        super(ResponseCode.NOT_FOUND_COMMENT);
    }
}
