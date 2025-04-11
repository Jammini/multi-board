package study.multiproject.file.application.response;

import study.multiproject.file.domain.UploadFile;

public record FileResponse(
    Long id,
    String fileName,
    String downloadUrl,
    long fileSize,
    String filePath
) {

    public FileResponse(UploadFile file) {
        this(file.getId(), file.getOriginalName(), "/files/" + file.getId(), file.getFileSize(),
            file.getFilePath());
    }
}
