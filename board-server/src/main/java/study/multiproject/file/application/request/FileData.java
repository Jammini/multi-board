package study.multiproject.file.application.request;

import java.io.InputStream;

public record FileData(String fileName, InputStream content) {

}
