package study.multiproject.category.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.multiproject.category.application.response.CategoryResponse;
import study.multiproject.category.dao.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAllCategory() {
        return categoryRepository.findAll().stream()
                   .map(category -> new CategoryResponse(category.getId(), category.getName(),
                       category.getDescription(), category.getDisplayOrder()))
                   .toList();
    }

}
