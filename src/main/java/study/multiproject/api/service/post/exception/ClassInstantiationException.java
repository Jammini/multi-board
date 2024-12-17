package study.multiproject.api.service.post.exception;

import study.multiproject.api.error.exception.BaseException;
import study.multiproject.api.error.exception.ResponseCode;

public class ClassInstantiationException extends BaseException {

    public ClassInstantiationException() {
        super(ResponseCode.CLASS_INSTANTIATION_ERROR);
    }
}
