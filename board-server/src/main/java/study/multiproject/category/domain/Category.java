package study.multiproject.category.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
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


    @Builder
    private Category(String name, String description, Long displayOrder) {
        this.name = name;
        this.description = description;
        this.displayOrder = displayOrder;
    }
}
