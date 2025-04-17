package study.multiproject.post.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import study.multiproject.file.domain.UploadFile;
import study.multiproject.global.entity.BaseEntity;
import study.multiproject.user.domain.User;

@Getter
@Entity
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

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

    /**
     * 비밀글 여부
     */
    private boolean isSecret;

    /**
     * 삭제 여부
     */
    private boolean isDeleted;

    /**
     * 해시태그
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostHashtag> postHashtags;

    /**
     * 첨부파일
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UploadFile> uploadFiles;

    /**
     * 작성자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    protected Post(String title, String content, boolean isSecret, User user) {
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.isSecret = isSecret;
        this.isDeleted = false;
        this.postHashtags = new HashSet<>();
        this.uploadFiles = new HashSet<>();
        this.user = user;
    }

    public void changeViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                   .title(title)
                   .content(content)
                   .isSecret(isSecret);
    }

    public void edit(PostEditor postEditor) {
        this.title = postEditor.getTitle();
        this.content = postEditor.getContent();
        this.isSecret = postEditor.isSecret();
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
