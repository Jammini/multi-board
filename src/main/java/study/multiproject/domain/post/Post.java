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
import study.multiproject.domain.file.UploadFile;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    /**
     * 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 제목
     */
    private String title;

    /**
     * 내용
     */
    @Lob
    private String content;

    /**
     * 조회수
     */
    private long viewCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostHashtag> postHashtags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UploadFile> uploadFiles;

    @Builder
    protected Post(String title, String content) {
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.postHashtags = new HashSet<>();
        this.uploadFiles = new HashSet<>();
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

    public void addFile(UploadFile uploadFile) {
        uploadFiles.add(uploadFile);
        uploadFile.setPost(this);
    }

    public void removeFileById(Long fileId) {
        uploadFiles.removeIf(file -> file.getId().equals(fileId));
    }
}
