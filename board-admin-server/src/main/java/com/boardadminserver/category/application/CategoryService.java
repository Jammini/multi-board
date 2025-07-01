package com.boardadminserver.category.application;

import com.boardadminserver.category.application.request.CategoryServiceRequest;
import com.boardadminserver.category.application.request.CategoryUpdateServiceRequest;
import com.boardadminserver.category.application.response.CategoryResponse;
import com.boardadminserver.category.dao.CategoryRepository;
import com.boardadminserver.category.domain.Category;
import com.boardadminserver.category.exception.CategoryNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
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
                       category.getDescription(), category.isAttachmentsEnabled(), category.isSecretEnabled(), category.isHashtagsEnabled()))
                   .toList();
    }

    @Transactional
    public void updateCategory(CategoryUpdateServiceRequest request) {
        Category category = categoryRepository.findById(request.id()).orElseThrow(() ->new CategoryNotFoundException("해당 카테고리를 찾을 수 없습니다."));
        category.updateCategory(request.name(), request.description(), request.attachmentsEnabled(), request.secretEnabled(), request.hashtagsEnabled());
    }
}
