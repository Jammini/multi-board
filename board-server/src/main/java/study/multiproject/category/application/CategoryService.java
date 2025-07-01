package study.multiproject.category.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.category.application.response.CategoryResponse;
import study.multiproject.category.dao.CategoryRepository;
import study.multiproject.category.domain.Category;
import study.multiproject.category.exception.CategoryNotFoundException;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategory() {
        return categoryRepository.findAll().stream()
                   .map(CategoryService::entityToDto)
                   .toList();
    }

    /**
     * ID로 카테고리를 조회합니다.
     */
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findById(id)
                   .map(CategoryService::entityToDto)
                   .orElseThrow(CategoryNotFoundException::new);
    }

    /**
     * 카테고리 엔티티를 DTO로 변환합니다.
     */
    private static CategoryResponse entityToDto(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.getDisplayOrder(),
            category.isAttachmentsEnabled(),
            category.isSecretEnabled(),
            category.isHashtagsEnabled()
        );
    }
}
