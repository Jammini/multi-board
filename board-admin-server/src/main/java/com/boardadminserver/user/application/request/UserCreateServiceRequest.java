package com.boardadminserver.user.application.request;

public record UserCreateServiceRequest(
    String email,
    String password,
    String name,
    String nickname
) {

}