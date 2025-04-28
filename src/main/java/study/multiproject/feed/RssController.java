package study.multiproject.feed;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.ZoneId;
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
public class RssController {

    private final PostService postService;

    @GetMapping(value = "/rss", produces = MediaType.APPLICATION_RSS_XML_VALUE)
    public Channel getRssFeed(HttpServletRequest request) throws Exception {
        List<PostResponse> posts = postService.getList();

        Channel channel = new Channel("rss_2.0");
        channel.setTitle("게시판 최신 글");
        channel.setLink("https://github.com/Jammini/multi-board");
        channel.setDescription("게시판에서 제공하는 최신 글 피드");
        channel.setLanguage("ko");

        List<Item> items = new ArrayList<>();
        for (PostResponse post : posts) {
            Item item = new Item();
            item.setTitle(post.title());
            Description desc = new Description();
            desc.setValue(post.content());
            item.setDescription(desc);
            item.setPubDate(Date.from(post.createdAt().atZone(ZoneId.systemDefault()).toInstant()));
            items.add(item);
        }
        channel.setItems(items);
        return channel;
    }
}
