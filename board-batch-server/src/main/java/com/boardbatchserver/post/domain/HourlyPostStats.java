package com.boardbatchserver.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HourlyPostStats {

    /**
     * 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 통계 날짜
     */
    private LocalDate date;

    /**
     * 시간대 (0-23)
     */
    private int hour;

    /**
     * 해당 시간대에 생성된 게시글 수
     */
    private Long postCount;

    /**
     * 이전 시간대의 마지막 게시글 ID
     */
    private Long beforePostId;

    /**
     * 생성 시간
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    protected HourlyPostStats(LocalDate date, int hour, Long postCount, Long beforePostId) {
        this.date = date;
        this.hour = hour;
        this.postCount = postCount;
        this.beforePostId = beforePostId;
    }
}
