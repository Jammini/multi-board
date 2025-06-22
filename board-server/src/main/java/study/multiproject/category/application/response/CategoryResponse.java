package study.multiproject.category.application.response;

public record CategoryResponse(
    Long id,
    String title,
    String description,
    Long displayOrder
) {

}
