package com.boardadminserver.poststatus.api;

import com.boardadminserver.poststatus.application.PostStatusService;
import com.boardadminserver.poststatus.application.response.DailyPostStatsResponse;
import com.boardadminserver.poststatus.application.response.HourlyPostStatsResponse;
import com.boardadminserver.poststatus.application.response.WeeklyPostStatsResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/stats/posts")
@RequiredArgsConstructor
public class PostStatsController {

    private final PostStatusService postStatusService;

    /**
     * 시간별 게시글 통계 조회
     */
    @GetMapping("/hourly")
    public List<HourlyPostStatsResponse> getHourlyStats(@RequestParam LocalDate date) {
        return postStatusService.getHourlyStats(date);
    }

    /**
     * 일별 게시글 통계 조회
     */
    @GetMapping("/daily")
    public List<DailyPostStatsResponse> getDailyStats(
        @RequestParam LocalDate start,
        @RequestParam LocalDate end
    ) {
        return postStatusService.getDailyStats(start, end);
    }

    /**
     * 주간 게시글 통계 조회
     */
    @GetMapping("/weekly")
    public WeeklyPostStatsResponse getWeeklyStats(@RequestParam(defaultValue = "0") int weeks) {
        return postStatusService.getWeeklyStats(weeks);
    }
}
