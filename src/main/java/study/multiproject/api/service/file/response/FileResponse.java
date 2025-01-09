package study.multiproject.api.service.file.response;

import study.multiproject.domain.file.UploadFile;

public record FileResponse(
    Long id,
    String fileName,
    String downloadUrl
) {
    public FileResponse(UploadFile file) {
        this(file.getId(), file.getOriginalName(), "/files/" + file.getId());
    }
}
