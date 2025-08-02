package com.boardbatchserver.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HourlyPostStatsScheduler {

    private final HourlyPostStatsService hourlyPostStatsService;

    /**
     * 매시간 1분마다 실행되는 스케줄러
     */
    @Scheduled(cron = "0 1 * * * *")
    public void runHourlyPostStatsJob() {
        hourlyPostStatsService.generateHourlyStats();
    }
}
