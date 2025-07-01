package study.multiproject.category.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    /**
     * 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 카테고리 이름
     */
    private String name;

    /**
     * 카테고리 설명
     */
    private String description;

    /**
     * 카테고리 표시 순서
     */
    private Long displayOrder;

    /**
     * 첨부파일 업로드 가능 여부
     */
    private boolean attachmentsEnabled;

    /**
     * 비밀글 작성 가능 여부
     */
    private boolean secretEnabled;

    /**
     * 해시태그 사용 가능 여부
     */
    private boolean hashtagsEnabled;
}
