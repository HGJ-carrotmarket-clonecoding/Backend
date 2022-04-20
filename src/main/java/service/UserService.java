package service;


import domain.User;
import dto.requestDto.SignupRequestDto;
import dto.requestDto.UserRequestDto;
import exception.EmptyException;
import exception.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import security.JwtTokenProvider;
import util.SignupValidator;

@RequiredArgsConstructor
@Service
public class UserService {
    //주입
    private final SignupValidator signupValidator;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    //회원가입 시 이메일이 사용할수있는지 없는지 확인
    public boolean checkDupEmail(String email) {
        return signupValidator.validate(email);
    }
    //회원가입 시 성공적으로 회원가입이 되면 토큰생성
    public String createToken(UserRequestDto userRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userRequestDto.getEmail(), userRequestDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.createToken(authentication);
    }

    //로그인 시 이메일 이나 비밀번호 를 입력을 안하거나 잘못 입력했을시 예외 처리
    public User loginValidCheck(UserRequestDto userRequestDto) {
        String email = userRequestDto.getEmail().trim();
        String password = userRequestDto.getPassword().trim();
        if (email.equals("") || password.equals(""))
            throw new EmptyException("로그인 정보를 모두 입력해주세요.");
        User userEmail = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("회원정보가 일치하지 않습니다. 다시 입력해주세요."));
        if (!passwordEncoder.matches(password, userEmail.getPassword())) {
            throw new UsernameNotFoundException("회원정보가 일치하지 않습니다. 다시 입력해주세요.");
        }
        return userEmail;
    }
    public User loadUserEamil(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("로그인 정보가 존재하지 않습니다.")
        );
    }

    //회원가입을 할때  무조건 이메일 비밀번호 유저이름 필수 로 입력해야함
    public boolean registerUser(String email, String password, String username) {
        User user = signupValidator.validate(new SignupRequestDto(email, password, username));
        userRepository.save(user);
        return true;
    }
    //로그인 시 무조건 이메일 비밀번호 입력을 안할시 예외처리
    public User loginValidCheck(String email, String password) {
        if (email.equals("") || password.equals(""))
            throw new EmptyException("로그인 정보를 모두 입력해주세요.");
        User userEmail = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("회원정보가 일치하지 않습니다. 다시 입력해주세요."));
        if (!passwordEncoder.matches(password, userEmail.getPassword())) {
            throw new UsernameNotFoundException("회원정보가 일치하지 않습니다. 다시 입력해주세요.");
        }
        return userEmail;
    }
    //이메일 과 비밀번호가 일치 할시 겍체형태로 인증된 토큰 과 권한을 봔환함
    public String createToken(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.createToken(authentication);
    }
}
