package study.multiproject.file.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class FileConversionException extends BaseException {

    public FileConversionException(String message) {
        super(ResponseCode.FILE_CONVERSION_ERROR, message);
    }
}
