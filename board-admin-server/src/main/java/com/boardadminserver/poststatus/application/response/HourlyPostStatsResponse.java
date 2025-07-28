package com.boardadminserver.poststatus.application.response;

public record HourlyPostStatsResponse(
    int hour,
    long postCount
) {

}
