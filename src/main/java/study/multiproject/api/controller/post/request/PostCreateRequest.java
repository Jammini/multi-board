package study.multiproject.api.controller.post.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import study.multiproject.api.controller.file.converter.FileDataConverter;
import study.multiproject.api.service.file.request.FileData;

public record PostCreateRequest(
    @NotBlank(message = "제목은 필수입니다.") String title,
    @NotBlank(message = "내용은 필수입니다.") String content,
    List<String> hashtags,
    List<MultipartFile> files
) {

    public List<FileData> toFileDataList() {
        return FileDataConverter.toFileDataList(files);
    }
}
