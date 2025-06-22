package com.boardadminserver.admin.application.response;

public record AdminResponse(
    Long id,
    String email,
    String password,
    String name
) {

}
