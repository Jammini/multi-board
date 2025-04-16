package study.multiproject.feed;

import static study.multiproject.global.util.TimeParsingUtils.formatterLocalDateTime;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import com.rometools.rome.io.WireFeedOutput;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.multiproject.post.application.PostService;
import study.multiproject.post.application.response.PostResponse;

@RestController
@RequiredArgsConstructor
public class RssController {

    private final PostService postService;

    @GetMapping(value = "/rss", produces = "application/rss+xml")
    public void getRssFeed(HttpServletResponse response) throws Exception {
        List<PostResponse> posts = postService.getList();

        Channel channel = new Channel("rss_2.0");
        channel.setTitle("게시판 최신 글");
        channel.setLink("https://mydomain.com");
        channel.setDescription("게시판에서 제공하는 최신 글 피드");
        channel.setLanguage("ko");

        List<Item> items = new ArrayList<>();
        for (PostResponse post : posts) {
            Item item = new Item();
            item.setTitle(post.title());
            item.setLink("https://mydomain.com/posts/" + post.id());

            Description desc = new Description();
            desc.setValue(post.content());
            item.setDescription(desc);

            item.setPubDate(Date.from(formatterLocalDateTime(post.createdAt()).atZone(ZoneId.systemDefault()).toInstant()));

            items.add(item);
        }

        channel.setItems(items);

        // 응답에 RSS XML 출력
        response.setCharacterEncoding("UTF-8");
        try (OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), "UTF-8")) {
            WireFeedOutput output = new WireFeedOutput();
            output.output(channel, writer);
        }
    }
}
