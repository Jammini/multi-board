package study.multiproject.user.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.multiproject.comment.domain.Comment;
import study.multiproject.file.domain.UploadFile;
import study.multiproject.post.domain.Post;

@Table(name = "users")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    /**
     * 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 이메일
     */
    private String email;

    /**
     * 비밀번호
     */
    private String password;

    /**
     * 이름
     */
    private String name;

    /**
     * 닉네임
     */
    private String nickname;

    /**
     * 생성일시
     */
    private LocalDateTime createdAt;

    /**
     * 프로필 아이디
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_file_id")
    private UploadFile profileImage;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> post;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comment;

    @Builder
    private User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.post = new HashSet<>();
        this.comment = new HashSet<>();
    }

    public void updateNickname(String newNickname) {
        if (newNickname != null){
            this.nickname = newNickname;
        }
    }

    public void updateProfileImage(UploadFile newImage) {
        if (newImage != null) {
            this.profileImage = newImage;
            newImage.setUser(this);
        }
    }

    public void clearProfileImage() {
        this.profileImage.setUser(null);
        this.profileImage = null;
    }

    public void changePassword(String password) {
        this.password = password;
    }
}
