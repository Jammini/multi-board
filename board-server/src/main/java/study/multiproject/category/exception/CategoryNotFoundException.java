package study.multiproject.category.exception;

import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

public class CategoryNotFoundException extends BaseException {

    public CategoryNotFoundException() {
        super(ResponseCode.NOT_FOUND_CATEGORY);
    }
}
