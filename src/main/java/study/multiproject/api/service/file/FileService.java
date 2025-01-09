package study.multiproject.api.service.file;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import study.multiproject.api.error.exception.ResponseCode;
import study.multiproject.api.service.file.exception.FileNotFoundException;
import study.multiproject.api.service.post.exception.FileStorageException;
import study.multiproject.domain.file.UploadFile;
import study.multiproject.domain.file.UploadFileRepository;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.dir}")
    private String fileDirectory;

    private final UploadFileRepository uploadFileRepository;

    /**
     * 파일 저장
     */
    public UploadFile storeFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);
        try {
            file.transferTo(new File(getFullPath(storeFileName)));
        } catch (IOException e) {
            throw new FileStorageException(ResponseCode.FILE_SAVE_ERROR);
        }
        return UploadFile.builder()
                   .fileName(storeFileName)
                   .originalName(originalFileName)
                   .filePath(getFullPath(storeFileName))
                   .fileSize(file.getSize())
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
            throw new FileStorageException(ResponseCode.FILE_LOAD_ERROR);
        }
    }

    /**
     * 파일 조회
     */
    public UploadFile getFileById(Long fileId) {
        return uploadFileRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    }

    public void deleteFile(Long fileId) {
        uploadFileRepository.deleteById(fileId);
    }

    /**
     * 파일 전체 경로
     */
    public String getFullPath(String fileName) {
        return fileDirectory + fileName;
    }

    /**
     * 파일명에서 확장자 추출
     */
    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

    /**
     * 파일명 생성
     */
    private String createStoreFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
}
