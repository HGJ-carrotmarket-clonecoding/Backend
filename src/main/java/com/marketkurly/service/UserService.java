package com.marketkurly.service;

import com.marketkurly.dto.requestDto.SignupRequestDto;
import com.marketkurly.dto.requestDto.UserRequestDto;
import com.marketkurly.exception.EmptyException;
import com.marketkurly.model.User;
import com.marketkurly.repository.UserRepository;
import com.marketkurly.security.JwtTokenProvider;
import com.marketkurly.util.SignupValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final SignupValidator signupValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public boolean checkDupEmail(String email){

        return signupValidator.validate(email);
    }

    public User loginValidCheck(UserRequestDto userRequestDto){
    String email = userRequestDto.getEmail().trim();
    String password = userRequestDto.getPassword().trim();

    if(email.equals("") || password.equals(""))
        throw new EmptyException("로그인 정보를 모두 입력해주세요.");

    User userEmail = userRepository.findByEmail(email).orElseThrow(
            ()->new UsernameNotFoundException("회원정보가 일치하지 않습니다. 다시 입력해주세요."));

    if (!passwordEncoder.matches(password, userEmail.getPassword())){
        throw new UsernameNotFoundException("회원정보가 일치하지 않습니다. 다시 입력해주세요.");
        }
    return userEmail;
    }

    public boolean registerUser(String email,String password,String username){
    User user = signupValidator.validate(new SignupRequestDto(email, password, username));
    userRepository.save(user);
    return true;
    }

    public User loadUserEamil(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("로그인 정보가 존재하지 않습니다.")
        );
    }

    public User loginValidCheck(String email, String password) {
        if(email.equals("") || password.equals(""))
            throw new EmptyException("로그인 정보를 모두 입력해주세요.");

        User userEmail = userRepository.findByEmail(email).orElseThrow(
                ()->new UsernameNotFoundException("회원정보가 일치하지 않습니다. 다시 입력해주세요."));

        if (!passwordEncoder.matches(password, userEmail.getPassword())){
            throw new UsernameNotFoundException("회원정보가 일치하지 않습니다. 다시 입력해주세요.");
        }
        return userEmail;
    }


   public String createToken(String email, String password){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email,password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);//인증객체만들수 있게 제공
        return jwtTokenProvider.createToken(authentication);
    }


}
