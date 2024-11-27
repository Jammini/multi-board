package study.multiproject.api.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common
    OK(200, "C001", "요청에 성공하였습니다."),

    // POST
    NOT_FOUND_POST(404, "P001", "존재하지 않는 글입니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}