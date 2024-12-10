package study.multiproject.api.service.post.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import study.multiproject.api.service.post.exception.ClassInstantiationException;

@Getter
public class PagingResponse<T> {

    private final long page;
    private final long size;
    private final long totalCount;
    private final List<T> items;

    @Builder
    public PagingResponse(Page<?> page, Class<T> clazz) {
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
        this.totalCount = page.getTotalElements();
        this.items = page.map(content -> {
            try {
                return clazz.getDeclaredConstructor(content.getClass()).newInstance(content);
            } catch (Exception e) {
                throw new ClassInstantiationException();
            }
        }).toList();
    }
}
