package study.multiproject.api.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.api.service.user.exception.AlreadyExistsEmailException;
import study.multiproject.api.service.user.exception.UserNotFoundException;
import study.multiproject.api.service.user.request.UserSignupServiceRequest;
import study.multiproject.api.service.user.response.UserResponse;
import study.multiproject.domain.user.User;
import study.multiproject.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
     * 이메일 중복 체크
     */
    private void checkEmailDuplication(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new AlreadyExistsEmailException();
        }
    }

}
