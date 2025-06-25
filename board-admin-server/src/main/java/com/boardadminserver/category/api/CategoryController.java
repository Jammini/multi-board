package com.boardadminserver.category.api;

import com.boardadminserver.category.api.converter.CategoryRequestConverter;
import com.boardadminserver.category.api.request.CategoryRequest;
import com.boardadminserver.category.application.CategoryService;
import com.boardadminserver.category.application.response.CategoryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRequestConverter categoryRequestConverter;

    @PostMapping("/category")
    public void createCategory(@RequestBody CategoryRequest categoryRequest) {
        categoryService.createCategory(categoryRequestConverter.toServiceRequest(categoryRequest));
    }

    @GetMapping("/categories")
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
