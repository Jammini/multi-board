package com.boardadminserver.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    /**
     * 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 이메일
     */
    private String email;

    /**
     * 비밀번호
     */
    private String password;

    /**
     * 이름
     */
    private String name;

    /**
     * 닉네임
     */
    private String nickname;

    /**
     * 생성일시
     */
    private LocalDateTime createdAt;

    /**
     * 활성화 상태
     */
    private boolean active;

    /**
     * 활성화 업데이트
     */
    public void activate() {
        this.active = true;
    }

    /**
     * 비활성화 업데이트
     */
    public void deactivate() {
        this.active = false;
    }
}
