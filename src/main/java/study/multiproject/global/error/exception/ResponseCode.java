package study.multiproject.global.error.exception;

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

    // COMMENT
    NOT_FOUND_COMMENT(404, "C001", "존재하지 않는 댓글입니다."),
    NOT_FOUND_PARENT_COMMENT(404, "C002", "부모 댓글이 존재하지 않습니다."),
    COMMENT_DEPTH_OVERFLOW(400, "C103", "댓글의 최대 깊이를 초과하였습니다."),
    COMMENT_CHUNK_OVERFLOW(400, "C104", "댓글의 최대 개수를 초과하였습니다."),

    // USER
    ALREADY_EXISTS_EMAIL(400, "U001", "이미 가입된 이메일입니다."),
    INVALID_USER_INFO(401, "U002", "아이디 혹은 비밀번호가 올바르지 않습니다."),
    NOT_FOUND_USER(404, "U003", "존재하지 않는 사용자입니다."),
    UNAUTHORIZED(401, "U003", "로그인이 필요합니다."),
    FORBIDDEN(403, "U004", "접근 권한이 없습니다."),

    // SHORTEN_URL
    NOT_FOUND_SHORTEN_URL(404, "S001", "존재하지 않는 단축 URL입니다."),
    LACK_OF_SHORTEN_URL_KEY(400, "S002", "단축 URL 키가 없습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
