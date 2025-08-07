package com.boardadminserver.poststatus.application.response;

import java.time.LocalDate;

public record WeeklyPostStatsResponse(
    LocalDate startDate,
    LocalDate endDate,
    long totalPostCount
) {

}
