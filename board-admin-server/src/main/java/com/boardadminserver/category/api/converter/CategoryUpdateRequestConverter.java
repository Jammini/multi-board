package com.boardadminserver.category.api.converter;

import com.boardadminserver.category.api.request.CategoryUpdateRequest;
import com.boardadminserver.category.application.request.CategoryUpdateServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class CategoryUpdateRequestConverter {

    public CategoryUpdateServiceRequest toServiceRequest(CategoryUpdateRequest request) {
        return new CategoryUpdateServiceRequest(
            request.id(),
            request.name(),
            request.description(),
            request.attachmentsEnabled(),
            request.secretEnabled(),
            request.hashtagsEnabled()
        );
    }
}
