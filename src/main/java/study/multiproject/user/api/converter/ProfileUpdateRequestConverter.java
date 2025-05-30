package study.multiproject.user.api.converter;

import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import study.multiproject.file.application.request.FileData;
import study.multiproject.file.exception.FileConversionException;
import study.multiproject.user.application.request.ProfileUpdateServiceRequest;

@Component
public class ProfileUpdateRequestConverter {

    public ProfileUpdateServiceRequest toServiceRequest(String nickname, boolean removePhoto, Long fileId) {
        return new ProfileUpdateServiceRequest(nickname, removePhoto, fileId);
    }

    public FileData toServiceRequest(MultipartFile file) {
        try {
            return new FileData(file.getOriginalFilename(), file.getInputStream());
        } catch (IOException e) {
            throw new FileConversionException(e.getMessage());
        }
    }
}
