package study.multiproject.category.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.multiproject.category.application.CategoryService;
import study.multiproject.category.application.response.CategoryResponse;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 카테고리 목록을 조회합니다.
     */
    @GetMapping("/category")
    public List<CategoryResponse> getAllCategory() {
        return categoryService.getAllCategory();
    }

    /**
     * ID로 카테고리를 조회합니다.
     */
    @GetMapping("/category/{categoryId}")
    public CategoryResponse getCategory(@PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }
}
