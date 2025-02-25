package study.multiproject.domain.comment.exception;

import study.multiproject.api.error.exception.BaseException;
import study.multiproject.api.error.exception.ResponseCode;

public class ChunkOverflowException extends BaseException {

    public ChunkOverflowException() {
        super(ResponseCode.COMMENT_CHUNK_OVERFLOW);
    }
}
