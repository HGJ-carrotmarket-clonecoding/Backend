package com.marketkurly.service;

import com.marketkurly.dto.requsetDto.SignupRequestDto;
import com.marketkurly.model.User;
import com.marketkurly.repository.UserRepository;
import com.marketkurly.util.SignupValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final SignupValidator signupValidator;
    private final UserRepository userRepository;

    public boolean registerUser(String email, String password, String username) {
        User user = signupValidator.validate(new SignupRequestDto(email, password, username));
        userRepository.save(user);
        return true;
    }

}
