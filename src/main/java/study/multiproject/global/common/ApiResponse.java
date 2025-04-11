package study.multiproject.global.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import study.multiproject.global.error.exception.ResponseCode;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private HttpStatus status;
    private String message;
    private T data;

    @Builder
    public ApiResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                   .status(HttpStatus.OK)
                   .message(ResponseCode.OK.getMessage())
                   .data(data)
                   .build();
    }

    public static <T> ApiResponse<T> fail(HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                   .status(status)
                   .message(message)
                   .build();
    }

    public static <T> ApiResponse<T> fail(ResponseCode responseCode) {
        return ApiResponse.<T>builder()
                   .status(HttpStatus.valueOf(responseCode.getStatus()))
                   .message(responseCode.getMessage())
                   .build();
    }
}
