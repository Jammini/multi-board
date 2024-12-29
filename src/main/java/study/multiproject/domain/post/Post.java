package study.multiproject.domain.post;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    private long viewCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostHashtag> postHashtags;

    @Builder
    protected Post(String title, String content) {
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.postHashtags = new HashSet<>();
    }

    public void changeViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                   .title(title)
                   .content(content);
    }

    public void edit(PostEditor postEditor) {
        this.title = postEditor.getTitle();
        this.content = postEditor.getContent();
    }

    public void addPostHashtag(PostHashtag postHashtag) {
        this.postHashtags.add(postHashtag);
        postHashtag.addPost(this);
    }
}
