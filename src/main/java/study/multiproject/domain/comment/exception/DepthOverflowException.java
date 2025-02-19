package study.multiproject.domain.comment.exception;

import study.multiproject.api.error.exception.BaseException;
import study.multiproject.api.error.exception.ResponseCode;

public class DepthOverflowException extends BaseException {

    public DepthOverflowException() {
        super(ResponseCode.COMMENT_DEPTH_OVERFLOW);
    }
}
