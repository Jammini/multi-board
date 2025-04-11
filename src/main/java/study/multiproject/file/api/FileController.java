package study.multiproject.file.api;

import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import study.multiproject.global.common.ApiResponse;
import study.multiproject.file.api.converter.FileDataConverter;
import study.multiproject.file.application.FileService;
import study.multiproject.file.application.response.FileResponse;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 파일 다운로드
     */
    @GetMapping("/files/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable Long fileId) {
        FileResponse response = fileService.getFileById(fileId);
        Resource resource = fileService.loadAsResource(response.filePath());
        String encodeUploadFileName = UriUtils.encode(response.fileName(),
            StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                   .headers(createHeaders(encodeUploadFileName, response.fileSize()))
                   .contentType(MediaType.APPLICATION_OCTET_STREAM)
                   .body(resource);
    }

    /**
     * 파일 업로드
     */
    @PostMapping("/files")
    public ApiResponse<List<Long>> upload(@RequestParam("files") List<MultipartFile> files) {
        return ApiResponse.success(fileService.storeFiles(FileDataConverter.toFileDataList(files)));
    }

    /**
     * 게시글에서 파일 삭제
     */
    @DeleteMapping("/files/{fileId}")
    public ApiResponse<Void> deleteFile(@PathVariable Long fileId) {
        fileService.deleteFile(fileId);
        return ApiResponse.success(null);
    }

    /**
     * 파일 다운로드 헤더 생성
     */
    private HttpHeaders createHeaders(String encodedFileName, long fileSize) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
            String.format("attachment; filename=\"%s\"; filename*=UTF-8''%s", encodedFileName, encodedFileName));
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize));
        return headers;
    }
}
