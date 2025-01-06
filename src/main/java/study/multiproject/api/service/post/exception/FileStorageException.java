package study.multiproject.api.service.post.exception;

import study.multiproject.api.error.exception.BaseException;
import study.multiproject.api.error.exception.ResponseCode;

public class FileStorageException extends BaseException {

    public FileStorageException(ResponseCode responseCode) {
        super(responseCode);
    }

}
