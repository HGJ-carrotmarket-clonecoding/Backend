package com.marketkurly.security;

import com.marketkurly.model.User;
import com.marketkurly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    // DB에서 유저의 정보를 조회하는 역할을 수행합니다.
    // UserDetailsService 인터페이스에서 DB에서 유저정보를 불러오는 중요한 메소드는 loadUserByUsername() 메서드 입니다.

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository =userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)  {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("해당 email 주소의 유저를 찾을 수 없습니다."));

        return new UserDetailsImpl(user);
    }
}
