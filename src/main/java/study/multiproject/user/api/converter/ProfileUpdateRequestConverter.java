package study.multiproject.user.api.converter;

import java.io.IOException;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import study.multiproject.file.application.request.FileData;
import study.multiproject.file.exception.FileConversionException;
import study.multiproject.user.api.request.ProfileUpdateRequest;
import study.multiproject.user.application.request.ProfileUpdateServiceRequest;

@Component
public class ProfileUpdateRequestConverter {

    public ProfileUpdateServiceRequest toServiceRequest(ProfileUpdateRequest request) {
        MultipartFile file = request.file();
        Optional<FileData> fileData;

        if (file == null || file.isEmpty()) {
            fileData = Optional.empty();
        } else {
            try {
                fileData = Optional.of(new FileData(file.getOriginalFilename(), file.getInputStream()));
            } catch (IOException e) {
                throw new FileConversionException(e.getMessage());
            }
        }
        return new ProfileUpdateServiceRequest(request.nickname(), fileData);
    }
}
