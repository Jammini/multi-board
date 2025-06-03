package study.multiproject.comment.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class DepthOverflowException extends BaseException {

    public DepthOverflowException() {
        super(ResponseCode.COMMENT_DEPTH_OVERFLOW);
    }
}
