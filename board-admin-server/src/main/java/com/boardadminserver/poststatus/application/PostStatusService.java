package com.boardadminserver.poststatus.application;

import com.boardadminserver.poststatus.application.response.DailyPostStatsResponse;
import com.boardadminserver.poststatus.application.response.HourlyPostStatsResponse;
import com.boardadminserver.poststatus.application.response.WeeklyPostStatsResponse;
import com.boardadminserver.poststatus.dao.PostStatusRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostStatusService {

    private final PostStatusRepository postStatusRepository;

    /**
     * 시간대별 통계 조회
     */
    public List<HourlyPostStatsResponse> getHourlyStats(LocalDate date) {
        return postStatusRepository.findByDateOrderByHour(date).stream()
                   .map(stat -> new HourlyPostStatsResponse(stat.getHour(), stat.getPostCount()))
                   .toList();
    }

    /**
     * 일자별 통계 조회
     */
    public List<DailyPostStatsResponse> getDailyStats(LocalDate start, LocalDate end) {
        return postStatusRepository.findDailyPostStats(start, end);
    }

    /**
     * 주간 통계 조회
     */
    public WeeklyPostStatsResponse getWeeklyStats(int weeksAgo) {
        LocalDate today = LocalDate.now();
        LocalDate end = today.with(DayOfWeek.SUNDAY).minusWeeks(weeksAgo);
        LocalDate start = end.minusDays(6);

        Long total = postStatusRepository.sumPostCountBetween(start, end);
        if (total == null) total = 0L;

        return new WeeklyPostStatsResponse(start, end, total);
    }
}
