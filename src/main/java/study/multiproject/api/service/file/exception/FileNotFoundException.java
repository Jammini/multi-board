package study.multiproject.api.service.file.exception;

import study.multiproject.api.error.exception.BaseException;
import study.multiproject.api.error.exception.ResponseCode;

public class FileNotFoundException extends BaseException {

    public FileNotFoundException() {
        super(ResponseCode.NOT_FOUND_FILE);
    }
}
