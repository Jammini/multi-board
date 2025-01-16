package study.multiproject.api.controller.file.converter;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import study.multiproject.api.service.file.exception.FileConversionException;
import study.multiproject.api.service.file.request.FileData;

public class FileDataConverter {

    private FileDataConverter() {
    }

    public static List<FileData> toFileDataList(List<MultipartFile> files) {
        if (files == null) {
            return List.of();
        }
        return files.stream()
                   .map(file -> {
                       try {
                           return new FileData(file.getOriginalFilename(), file.getInputStream());
                       } catch (IOException e) {
                           throw new FileConversionException(e.getMessage());
                       }
                   })
                   .toList();
    }
}
