package study.multiproject.user.application.request;

import java.util.Optional;
import study.multiproject.file.application.request.FileData;

public record ProfileUpdateServiceRequest(
    String nickname,
    Optional<FileData> fileData
) {

}
