package study.multiproject.shortenurl.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class LackOfShortenUrlKeyException extends BaseException {

    public LackOfShortenUrlKeyException() {
        super(ResponseCode.LACK_OF_SHORTEN_URL_KEY);
    }
}
