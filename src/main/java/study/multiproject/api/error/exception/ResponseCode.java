package study.multiproject.api.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    // Common
    OK(200, "C001", "요청에 성공하였습니다."),

    // POST
    NOT_FOUND_POST(404, "P001", "존재하지 않는 글입니다."),
    CLASS_INSTANTIATION_ERROR(500, "P002", "클래스 인스턴스화에 실패하였습니다."),

    // FILE
    NOT_FOUND_FILE(404, "F001", "존재하지 않는 파일입니다."),
    FILE_SAVE_ERROR(500, "F002", "파일 저장에 실패했습니다."),
    FILE_LOAD_ERROR(500, "F003", "파일을 읽을 수 없습니다."),
    FILE_CONVERSION_ERROR(500, "F004", "파일 변환에 실패했습니다."),
    FILE_SIZE_ERROR(400, "F005", "파일 크기 확인에 실패했습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
