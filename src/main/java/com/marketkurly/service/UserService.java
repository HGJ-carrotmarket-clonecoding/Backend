package com.marketkurly.service;

import com.marketkurly.dto.requsetDto.SignupRequestDto;
import com.marketkurly.exception.EmptyException;
import com.marketkurly.model.User;
import com.marketkurly.repository.UserRepository;
import com.marketkurly.security.JwtTokenProvider;
import com.marketkurly.util.SignupValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final SignupValidator signupValidator;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    public boolean registerUser(String email, String password, String username,String address,String address_sub) {
        // 회원 가입 서비스
        User user = signupValidator.validate(new SignupRequestDto(email, password, username,address,address_sub));
        userRepository.save(user);
        return true;
    }

    public boolean checkDupEmail(String email) {
        // 이메일 중복확인 서비스
        return signupValidator.validate(email);
    }

    public User loginValidCheck(String email, String password) {
        // 유저 정보 확인 체크
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
        log.info("44444 : UserService.createToken");
        // 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.createToken(authentication);
    }

    public User loadUserEamil(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("로그인 정보가 존재하지 않습니다.")
        );
    }

}
