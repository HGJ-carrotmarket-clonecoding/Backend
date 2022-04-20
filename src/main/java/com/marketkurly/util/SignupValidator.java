package com.marketkurly.util;

import com.marketkurly.dto.requestDto.SignupRequestDto;
import com.marketkurly.exception.DuplicateUserException;
import com.marketkurly.model.User;
import com.marketkurly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SignupValidator {

    private final UserRepository userRepository;

    public User validate(SignupRequestDto signupRequestDto) {

        String email = signupRequestDto.getEmail().trim();
        String username = signupRequestDto.getUsername().trim();
        String password = signupRequestDto.getPassword().trim();
        Optional<User> userByEmail = userRepository.findByEmail(email);
        String pattern = "(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%^&*]*$";

        return User.builder()
                .email(email)
                .password(password)
                .username(username)
                .build();
    }

    public boolean validate(String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent())
            throw new DuplicateUserException("이메일이 중복되었습니다");
            return true;
        }

}
