package study.multiproject.api.error;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import study.multiproject.api.common.ApiResponse;
import study.multiproject.api.error.exception.BaseException;
import study.multiproject.api.error.exception.ErrorCode;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    private static String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private ApiResponse<ErrorCode> buildAndReturnResponse(HttpStatus status, String message) {
        String currentTime = getCurrentTime();
        log.error("[ Error ] {} : {}", currentTime, message);
        return ApiResponse.fail(status, message);
    }

    @ExceptionHandler(BaseException.class)
    public ApiResponse<ErrorCode> handleBaseException(BaseException exception) {
        return buildAndReturnResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<ErrorCode> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception) {
        return buildAndReturnResponse(HttpStatus.BAD_REQUEST,
            exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse<ErrorCode> handleException(Exception exception) {
        return buildAndReturnResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}
