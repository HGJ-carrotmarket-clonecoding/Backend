package util;


import domain.User;
import dto.requestDto.SignupRequestDto;
import exception.DuplicateUserException;
import exception.EmailFormException;
import exception.EmptyException;
import exception.LengthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import repository.UserRepository;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@RequiredArgsConstructor
public class SignupValidator {
    //주입
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원기입 할때 필요한 절차 로직
    public User validate(SignupRequestDto signupRequestDto) {

        String email = signupRequestDto.getEmail().trim(); // 이메일 에 불필요한 것 즉 중복값을 확인 해서 잘라준다
        String username = signupRequestDto.getUsername().trim();// 유저이름 에 불필요한 것 즉 중복값을 확인 해서 잘라준다
        String password = signupRequestDto.getPassword().trim();// 비밀번호 에 불필요한 것 즉 중복값을 확인 해서 잘라준다
        Optional<User> userByEmail = userRepository.findByEmail(email); // DB 에서 이메일 확인
        String pattern = "(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%^&*]*$"; // 이메일,유저이름 ,비밀번호 정규화 형식으로 패턴을 주고 이것만 허용한다

        if (email.equals("") || username.equals("") || password.equals("")) { // 셋중 하나가 값이 안들어오면 회원가입이 불가능
            throw new EmptyException("모든 내용을 입력해주세요.");
        } else if (userByEmail.isPresent()) { // 이메일이 중복이 있는지 확인
            throw new DuplicateUserException("이메일이 중복되었습니다.");
        } else if (!isValidEmail(email)) { // 이메일 형식으로 쓰지않을경우 예외
            throw new EmailFormException("이메일 형식으로 입력해주세요.");
        } else if ( (password.length() < 8 || password.length() > 16) || !Pattern.matches(pattern, password)) { // 비밀번호 는 만들어둔 패턴방식에 따라 첫글자 8 에서 16 글자 까지 쓸수있음
            throw new LengthException("비밀번호 8~16자리의 영문과 숫자를 조합해주세요.");
        }
        // 비밀번호 유출를 방지하고자 passwordEncoder 를 써서 암호화 시켜준다(보안떄문에)
        password = passwordEncoder.encode(password);
        return User.builder()
                .email(email)
                .password(password)
                .username(username).build();
    }
    //이메일 중복 확인 을 하는 함수
    public boolean validate(String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if(userByEmail.isPresent())
            throw new DuplicateUserException("이메일이 중복되었습니다.");
        return true;
    }
    //화원가입시 이메일 정규화 하는 함수
    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) { err = true; } return err;
    }

}
