package study.multiproject.api.service.file.request;

import java.io.InputStream;

public record FileData(String fileName, InputStream content) {

}
