package security;

import domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    //주입
    private UserRepository userRepository;
    //주입
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }
    //이메일 을 찾아 토큰 유무확인 하는 함수
    public UserDetails loadUserByUsername(String email){
        //회원가입 을 한 유저의 이메일 을 디비에서 확인 후 로그인 할때 이메일이 없으면 예외처러 하고 있으면 유저 데이터 리턴
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("해당 email 이 없습니다."));
        return  new UserDetailsImpl(user);

    }
}
