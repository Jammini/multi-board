package study.multiproject.feed;

import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Link;
import com.rometools.rome.io.WireFeedOutput;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AtomController {

    @GetMapping(value = "/atom", produces = "application/atom+xml")
    public void atom(HttpServletResponse response) throws Exception {
        Feed feed = new Feed("atom_1.0");
        feed.setTitle("My Atom Feed");
        feed.setUpdated(new Date());
        feed.setId("https://mydomain.com");

        Entry entry = new Entry();
        entry.setTitle("첫 번째 포스트");
        entry.setId("https://mydomain.com/posts/1");
        entry.setUpdated(new Date());
        entry.setSummary(new Content() {{
            setValue("내용 요약입니다.");
        }});
        entry.setAlternateLinks(List.of(new Link() {{
            setHref("https://mydomain.com/posts/1");
        }}));

        feed.setEntries(List.of(entry));

        response.setCharacterEncoding("UTF-8");
        try (OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream())) {
            new WireFeedOutput().output(feed, writer);
        }
    }
}
