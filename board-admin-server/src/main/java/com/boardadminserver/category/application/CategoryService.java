package com.boardadminserver.category.application;

import com.boardadminserver.category.application.request.CategoryServiceRequest;
import com.boardadminserver.category.application.response.CategoryResponse;
import com.boardadminserver.category.dao.CategoryRepository;
import com.boardadminserver.category.domain.Category;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void createCategory(CategoryServiceRequest request) {
        Category category = Category.builder()
                                .name(request.name())
                                .description(request.description())
                                .build();
        categoryRepository.save(category);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                   .map(category -> new CategoryResponse(category.getId(), category.getName(),
                       category.getDescription()))
                   .toList();
    }

}
