package com.boardbatchserver.post.dao;

import com.boardbatchserver.post.domain.HourlyPostStats;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HourlyPostStatsRepository extends JpaRepository<HourlyPostStats, Long> {

    boolean existsByDateAndHour(LocalDate date, int hour);

    Optional<HourlyPostStats> findByDateAndHour(LocalDate date, int hour);
}
