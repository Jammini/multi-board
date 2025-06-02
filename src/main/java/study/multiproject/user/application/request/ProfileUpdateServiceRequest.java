package study.multiproject.user.application.request;

public record ProfileUpdateServiceRequest(
    String nickname,
    boolean removePhoto,
    Long fileId
) {

}
