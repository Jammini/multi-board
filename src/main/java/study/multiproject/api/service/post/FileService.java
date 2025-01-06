package study.multiproject.api.service.post;

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
import org.springframework.web.multipart.MultipartFile;
import study.multiproject.api.error.exception.ResponseCode;
import study.multiproject.api.service.post.exception.FileStorageException;

@Service
@RequiredArgsConstructor
public class FileService {
    public String saveFile(MultipartFile file) {
        String directory = "uploads/";
        String filePath = directory + UUID.randomUUID();
        try {
            Files.copy(file.getInputStream(), Paths.get(filePath));
            return filePath;
        } catch (IOException e) {
            throw new FileStorageException(ResponseCode.FILE_SAVE_ERROR);
        }
    }

    public Resource loadAsResource(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new FileStorageException(ResponseCode.FILE_LOAD_ERROR);
        }
    }
}
