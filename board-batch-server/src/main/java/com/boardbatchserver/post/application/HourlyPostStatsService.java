package com.boardbatchserver.post.application;

import com.boardbatchserver.post.dao.HourlyPostStatsRepository;
import com.boardbatchserver.post.dao.PostRepository;
import com.boardbatchserver.post.domain.HourlyPostStats;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HourlyPostStatsService {

    private final HourlyPostStatsRepository hourlyStatsRepository;
    private final PostRepository postRepository;

    /**
     * 시간대별 통계 생성
     */
    @Transactional
    public void generateHourlyStats() {
        // 현재 시각의 1시간 전 기준으로 통계 계산
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusHours(1).withMinute(0).withSecond(0).withNano(0);
        LocalDate startDate = start.toLocalDate();
        int hour = start.getHour();

        // 중복 저장 방지
        if (hourlyStatsRepository.existsByDateAndHour(startDate, hour)) {
            return;
        }

        saveHourlyStats(start, startDate, hour);
    }

    /**
     * 시간대별 통계 저장
     */
    private void saveHourlyStats(LocalDateTime start, LocalDate startDate, int hour) {
        Long currentMaxId = postRepository.findMaxPostIdBefore(start);
        Long prevMaxId = hourlyStatsRepository
                             .findByDateAndHour(startDate, hour - 1)
                             .map(HourlyPostStats::getBeforePostId)
                             .orElse(0L); // 없으면 0

        long postCount = currentMaxId != null ? currentMaxId - prevMaxId : 0;

        hourlyStatsRepository.save(HourlyPostStats.builder()
                                       .date(startDate)
                                       .hour(hour)
                                       .postCount(postCount)
                                       .beforePostId(prevMaxId)
                                       .build());
    }
}
