package com.boardadminserver.poststatus.application.response;

import java.time.LocalDate;

public record DailyPostStatsResponse(
    LocalDate date,
    long postCount
) {

}
