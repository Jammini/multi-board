package study.multiproject.category.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import study.multiproject.category.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
