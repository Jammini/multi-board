package study.multiproject.api.service.file.exception;

import study.multiproject.api.error.exception.BaseException;
import study.multiproject.api.error.exception.ResponseCode;

public class FileConversionException extends BaseException {

    public FileConversionException(String message) {
        super(ResponseCode.FILE_CONVERSION_ERROR, message);
    }
}
