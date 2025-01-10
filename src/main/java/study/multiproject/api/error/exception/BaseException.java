package study.multiproject.api.error.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ResponseCode responseCode;
    private final String details;

    public BaseException(ResponseCode responseCode, String details) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
        this.details = details;
    }

    public BaseException(ResponseCode responseCode) {
        this(responseCode, "");
    }

}
