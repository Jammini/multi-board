package study.multiproject.api.service.post.response;

import java.util.List;
import org.springframework.data.domain.Page;
import study.multiproject.api.service.post.exception.ClassInstantiationException;

public record PagingResponse<T>(long page, long size, long totalCount, List<T> items) {

    public PagingResponse(Page<?> page, Class<T> clazz) {
        this(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.map(content -> {
            try {
                return clazz.getDeclaredConstructor(content.getClass()).newInstance(content);
            } catch (Exception e) {
                throw new ClassInstantiationException();
            }
        }).toList());
    }
}
