package study.multiproject.file.application;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import study.multiproject.global.error.exception.ResponseCode;
import study.multiproject.file.exception.FileNotFoundException;
import study.multiproject.file.application.request.FileData;
import study.multiproject.file.application.response.FileResponse;
import study.multiproject.post.exception.FileStorageException;
import study.multiproject.global.config.FileProperties;
import study.multiproject.file.domain.UploadFile;
import study.multiproject.file.dao.UploadFileRepository;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileProperties fileProperties;

    private final UploadFileRepository uploadFileRepository;

    public List<Long> storeFiles(List<FileData> files) {
        return files.stream()
                   .map(uploadFile -> storeSingleFile(uploadFile).getId())
                   .toList();
    }

    /**
     * 파일 저장
     */
    public UploadFile storeSingleFile(FileData file) {
        String originalFileName = file.fileName();
        String storeFileName = createStoreFileName(originalFileName);
        Path targetPath = Paths.get(getFullPath(storeFileName));
        try (InputStream inputStream = file.content()) {
            Files.copy(inputStream, targetPath);
        } catch (IOException e) {
            throw new FileStorageException(ResponseCode.FILE_SAVE_ERROR, e.getMessage());
        }
        UploadFile uploadFile = UploadFile.builder()
                                    .fileName(storeFileName)
                                    .originalName(originalFileName)
                                    .filePath(getFullPath(storeFileName))
                                    .fileSize(getFileSize(targetPath))
                                    .build();
        return uploadFileRepository.save(uploadFile);
    }

    /**
     * 파일 로드
     */
    public Resource loadAsResource(String filePath) {
        Path path = Paths.get(filePath);
        PathResource resource = new PathResource(path);
        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException();
        }
        return resource;
    }

    /**
     * 파일 조회
     */
    public FileResponse getFileById(Long fileId) {
        UploadFile uploadFile = uploadFileRepository.findById(fileId)
                                    .orElseThrow(FileNotFoundException::new);
        return new FileResponse(uploadFile);
    }

    /**
     * 파일 삭제
     */
    public void deleteFile(Long fileId) {
        uploadFileRepository.deleteById(fileId);
    }

    /**
     * 파일 삭제
     */
    public void deleteFile(UploadFile file) {
        uploadFileRepository.delete(file);
    }

    /**
     * 파일 엔티티 조회
     */
    public UploadFile getFileEntityById(Long fileId) {
        return uploadFileRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    }

    /**
     * 파일 전체 경로
     */
    public String getFullPath(String fileName) {
        String dir = fileProperties.getDir();
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        return dir + fileName;
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

    /**
     * 파일 크기 조회
     */
    private long getFileSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new FileStorageException(ResponseCode.FILE_SIZE_ERROR, e.getMessage());
        }
    }
}
