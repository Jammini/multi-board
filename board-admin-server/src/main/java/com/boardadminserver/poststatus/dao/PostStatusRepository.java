package com.boardadminserver.poststatus.dao;

import com.boardadminserver.poststatus.application.response.DailyPostStatsResponse;
import com.boardadminserver.poststatus.domain.HourlyPostStats;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostStatusRepository extends JpaRepository<HourlyPostStats, Long> {

    List<HourlyPostStats> findByDateOrderByHour(LocalDate date);

    @Query("""
        SELECT new com.boardadminserver.poststatus.application.response.DailyPostStatsResponse(h.date, SUM(h.postCount))
        FROM HourlyPostStats h
        WHERE h.date BETWEEN :start AND :end
        GROUP BY h.date
        ORDER BY h.date
    """)
    List<DailyPostStatsResponse> findDailyPostStats(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("""
        SELECT SUM(h.postCount)
        FROM HourlyPostStats h
        WHERE h.date BETWEEN :start AND :end
    """)
    Long sumPostCountBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
