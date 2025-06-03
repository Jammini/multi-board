package study.multiproject.comment.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class ChunkOverflowException extends BaseException {

    public ChunkOverflowException() {
        super(ResponseCode.COMMENT_CHUNK_OVERFLOW);
    }
}
