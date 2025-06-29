package study.multiproject.category.application.response;

public record CategoryResponse(
    Long id,
    String title,
    String description,
    Long displayOrder,
    boolean attachmentsEnabled,
    boolean secretEnabled,
    boolean hashtagsEnabled
) {

}
