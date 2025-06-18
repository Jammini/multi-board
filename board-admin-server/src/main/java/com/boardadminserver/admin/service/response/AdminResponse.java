package com.boardadminserver.admin.service.response;

public record AdminResponse(
    Long id,
    String email,
    String password,
    String name
) {

}
