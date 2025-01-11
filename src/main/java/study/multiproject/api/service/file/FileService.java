package study.multiproject.api.service.file;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import study.multiproject.api.error.exception.ResponseCode;
import study.multiproject.api.service.file.exception.FileNotFoundException;
import study.multiproject.api.service.file.request.FileData;
import study.multiproject.api.service.file.response.FileResponse;
import study.multiproject.api.service.post.exception.FileStorageException;
import study.multiproject.config.FileProperties;
import study.multiproject.domain.file.UploadFile;
import study.multiproject.domain.file.UploadFileRepository;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileProperties fileProperties;

    private final UploadFileRepository uploadFileRepository;

    /**
     * 파일 저장
     */
    public UploadFile storeFile(FileData file) {
        String originalFileName = file.fileName();
        String storeFileName = createStoreFileName(originalFileName);
        try {
            Files.write(Paths.get(getFullPath(storeFileName)), file.content());
        } catch (IOException e) {
            throw new FileStorageException(ResponseCode.FILE_SAVE_ERROR, e.getMessage());
        }
        return UploadFile.builder()
                   .fileName(storeFileName)
                   .originalName(originalFileName)
                   .filePath(getFullPath(storeFileName))
                   .fileSize(file.content().length)
                   .build();
    }

    /**
     * 파일 로드
     */
    public Resource loadAsResource(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new FileStorageException(ResponseCode.FILE_LOAD_ERROR, e.getMessage());
        }
    }

    /**
     * 파일 조회
     */
    public FileResponse getFileById(Long fileId) {
        UploadFile uploadFile = uploadFileRepository.findById(fileId)
                                    .orElseThrow(FileNotFoundException::new);
        return new FileResponse(uploadFile);
    }

    public void deleteFile(Long fileId) {
        uploadFileRepository.deleteById(fileId);
    }

    /**
     * 파일 전체 경로
     */
    public String getFullPath(String fileName) {
        return fileProperties.getDir() + fileName;
    }

    /**
     * 파일명에서 확장자 추출
     */
    private String extractExtension(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

    /**
     * 파일명 생성
     */
    private String createStoreFileName(String originalFileName) {
        String ext = extractExtension(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
}
