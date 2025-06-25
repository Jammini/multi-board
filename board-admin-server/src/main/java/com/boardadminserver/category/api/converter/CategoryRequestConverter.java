package com.boardadminserver.category.api.converter;

import com.boardadminserver.category.api.request.CategoryRequest;
import com.boardadminserver.category.application.request.CategoryServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class CategoryRequestConverter {

    public CategoryServiceRequest toServiceRequest(CategoryRequest request) {
        return new CategoryServiceRequest(
            request.name(),
            request.description()
        );
    }
}
