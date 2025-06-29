package study.multiproject.category.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.category.application.response.CategoryResponse;
import study.multiproject.category.dao.CategoryRepository;
import study.multiproject.category.exception.CategoryNotFoundException;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategory() {
        return categoryRepository.findAll().stream()
                   .map(category -> new CategoryResponse(category.getId(), category.getName(),
                       category.getDescription(), category.getDisplayOrder(),
                       category.isAttachmentsEnabled(), category.isSecretEnabled(),
                       category.isHashtagsEnabled()))
                   .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findById(id)
                   .map(category -> new CategoryResponse(category.getId(), category.getName(),
                       category.getDescription(), category.getDisplayOrder(),
                       category.isAttachmentsEnabled(), category.isSecretEnabled(),
                       category.isHashtagsEnabled()))
                   .orElseThrow(CategoryNotFoundException::new);
    }
}
