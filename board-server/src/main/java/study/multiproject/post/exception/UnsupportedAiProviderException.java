package study.multiproject.post.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class UnsupportedAiProviderException extends BaseException {

    public UnsupportedAiProviderException(String message) {
        super(ResponseCode.UNSUPPORTED_AI_PROVIDER, message);
    }
}
