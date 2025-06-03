package study.multiproject.file.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class FileNotFoundException extends BaseException {

    public FileNotFoundException() {
        super(ResponseCode.NOT_FOUND_FILE);
    }
}
