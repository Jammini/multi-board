package study.multiproject.global.error;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import study.multiproject.global.common.ApiResponse;
import study.multiproject.global.error.exception.BaseException;
import study.multiproject.global.error.exception.ResponseCode;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    private static String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private ApiResponse<ResponseCode> buildAndReturnResponse(HttpStatus status, String message) {
        log.error("[ Error ] : {}", message);
        return ApiResponse.fail(status, message);
    }

    @ExceptionHandler(BaseException.class)
    public ApiResponse<ResponseCode> handleBaseException(BaseException exception) {
        return buildAndReturnResponse(HttpStatus.valueOf(exception.getResponseCode().getStatus()), exception.getMessage() + " | " + exception.getDetails());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<ResponseCode> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception) {
        return buildAndReturnResponse(HttpStatus.BAD_REQUEST,
            exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse<ResponseCode> handleException(Exception exception) {
        log.error("internal server error", exception);
        return buildAndReturnResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}
