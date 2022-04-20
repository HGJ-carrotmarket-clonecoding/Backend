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
    // UserDetailsService 인터페이스에는 사용자 이름UserDetails 으로 사용자를 로드하는 메소드가 있으며
    // Spring Security가 인증 및 유효성 검사에 사용할 수 있는 객체를 반환합니다 .

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
