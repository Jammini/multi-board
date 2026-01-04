package study.multiproject.post.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class AiSummaryException extends BaseException {

    public AiSummaryException(String message) {
        super(ResponseCode.AI_SUMMARY_ERROR, message);
    }
}
