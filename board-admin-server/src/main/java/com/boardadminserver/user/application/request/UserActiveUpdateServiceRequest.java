package com.boardadminserver.user.application.request;

public record UserActiveUpdateServiceRequest(
    Long id,
    boolean isActive
) {

}
