package com.boardadminserver.category.application.response;

public record CategoryResponse(
    Long id,
    String title,
    String description,
    boolean attachmentsEnabled,
    boolean secretEnabled,
    boolean hashtagsEnabled
) {

}
