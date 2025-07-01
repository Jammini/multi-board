package com.boardadminserver.category.api.request;

public record CategoryUpdateRequest(
    Long id,
    String name,
    String description,
    boolean attachmentsEnabled,
    boolean secretEnabled,
    boolean hashtagsEnabled
) {

}
