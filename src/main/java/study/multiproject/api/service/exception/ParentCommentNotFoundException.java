package study.multiproject.api.service.exception;

import study.multiproject.api.error.exception.BaseException;
import study.multiproject.api.error.exception.ResponseCode;

public class ParentCommentNotFoundException extends BaseException {
    public ParentCommentNotFoundException() {
        super(ResponseCode.NOT_FOUND_COMMENT);
    }
}
