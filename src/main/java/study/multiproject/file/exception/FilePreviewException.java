package study.multiproject.file.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class FilePreviewException extends BaseException {

    public FilePreviewException(String message) {
        super(ResponseCode.FILE_PREVIEW_ERROR, message);
    }

}
