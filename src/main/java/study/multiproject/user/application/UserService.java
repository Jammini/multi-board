package study.multiproject.user.application;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.file.application.FileService;
import study.multiproject.file.domain.UploadFile;
import study.multiproject.user.application.request.ProfileUpdateServiceRequest;
import study.multiproject.user.exception.AlreadyExistsEmailException;
import study.multiproject.user.exception.UserNotFoundException;
import study.multiproject.user.application.request.UserSignupServiceRequest;
import study.multiproject.user.application.response.UserResponse;
import study.multiproject.user.domain.User;
import study.multiproject.user.dao.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;
    @PersistenceContext
    private final EntityManager em;

    /**
     * 회원가입
     */
    @Transactional
    public Long signup(UserSignupServiceRequest request) {
        checkEmailDuplication(request.email());
        User user = request.toEntity(passwordEncoder.encode(request.password()));
        User saveUser = userRepository.save(user);
        return saveUser.getId();
    }

    /**
     * 내 정보 조회
     */
    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return new UserResponse(user);
    }

    /**
     * 회원 정보 조회
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 회원 정보 조회
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 이메일 중복 체크
     */
    private void checkEmailDuplication(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new AlreadyExistsEmailException();
        }
    }
    /**
     * 프로필 수정
     */
    @Transactional
    public void updateProfile(Long userId, ProfileUpdateServiceRequest request) {
        User user = getUserById(userId);
        user.updateNickname(request.nickname());

        request.fileData().ifPresent(fileData -> {
            UploadFile oldProfileImage = user.getProfileImage();
            if (oldProfileImage != null) {
                user.clearProfileImage();
                fileService.deleteFile(oldProfileImage);
                em.flush();
            }

            UploadFile uploadFile = fileService.storeSingleFile(fileData);
            user.updateProfileImage(uploadFile);
        });

    public void changePassword(Long userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.changePassword(passwordEncoder.encode(password));
    }
}
