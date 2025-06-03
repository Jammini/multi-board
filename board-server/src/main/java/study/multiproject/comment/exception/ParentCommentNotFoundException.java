package study.multiproject.comment.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class ParentCommentNotFoundException extends BaseException {
    public ParentCommentNotFoundException() {
        super(ResponseCode.NOT_FOUND_PARENT_COMMENT);
    }
}
