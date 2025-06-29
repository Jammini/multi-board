package com.boardadminserver.category.application.request;

public record CategoryUpdateServiceRequest(
    Long id,
    String name,
    String description,
    boolean attachmentsEnabled,
    boolean secretEnabled,
    boolean hashtagsEnabled
) {

}
