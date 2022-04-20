package com.marketkurly.service;

import com.marketkurly.repository.UserRepository;
import com.marketkurly.util.SignupValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
@Service //서비스 레이어, 내부에서 자바 로직을 처리함
public class UserService {
    private final SignupValidator signupValidator;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

//    public boolean registerUser(SignupRequestDto signupRequestDto) {
//        User user = signupValidator.validate(signupRequestDto);
//        userRepository.save(user);
//        return true;
//    }

    public boolean checkDupEmail(String email) {
        return signupValidator.validate(email);
    }

    public String createToken(UserRequestDto userRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userRequestDto.getEmail(), userRequestDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.createToken(authentication);
    }

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

    public boolean registerUser(String email, String password, String username) {
        User user = signupValidator.validate(new SignupRequestDto(email, password, username));
        userRepository.save(user);
        return true;
    }

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

    public String createToken(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.createToken(authentication);
    }
}
