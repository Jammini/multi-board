package study.multiproject.shortenurl.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class NotFoundShortenUrlException extends BaseException {

    public NotFoundShortenUrlException() {
        super(ResponseCode.NOT_FOUND_SHORTEN_URL);
    }
}
