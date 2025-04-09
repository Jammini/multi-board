package study.multiproject.post.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class FileStorageException extends BaseException {

    public FileStorageException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }
}
