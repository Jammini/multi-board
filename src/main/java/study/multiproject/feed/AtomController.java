package study.multiproject.feed;

import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.multiproject.post.application.PostService;
import study.multiproject.post.application.response.PostResponse;

@RestController
@RequiredArgsConstructor
public class AtomController {

    private final PostService postService;

    @GetMapping(value = "/atom", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public Feed atom(HttpServletRequest request) throws Exception {
        List<PostResponse> posts = postService.getList();

        Feed feed = new Feed("atom_1.0");
        feed.setTitle("토이 프로젝트 - 게시판");
        feed.setUpdated(new Date());
        feed.setId("https://github.com/Jammini/multi-board");

        List<Entry> items = new ArrayList<>();
        for (PostResponse post : posts) {
            Entry entry = new Entry();
            entry.setTitle(post.title());
            entry.setSummary(new Content() {{
                setValue(post.content());
            }});
            items.add(entry);
        }
        feed.setEntries(items);
        return feed;
    }
}
