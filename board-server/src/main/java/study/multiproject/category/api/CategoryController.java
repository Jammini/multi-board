package study.multiproject.category.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.multiproject.category.application.CategoryService;
import study.multiproject.category.application.response.CategoryResponse;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public List<CategoryResponse> getAllCategory() {
        return categoryService.getAllCategory();
    }
}
