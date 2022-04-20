package security;

import domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;


public class UserDetailsImpl implements UserDetails {

    //주입
    private final User user;

    //생성자
    public UserDetailsImpl(User user) {
        this.user = user;
    }
    //Getter
    public User getUser() {
        return user;
    }
    //======================================================
    //계정 비밀번호
    @Override
    public String getPassword() {
        return user.getPassword() ;
    }
    //계정 이름
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    //계정이 만료되지 않았는지 확인 true 면 만료안된계정이라는 뜻 (초기값은 flase)
   @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //계정을 너무 오래 안써서 잠겨있는지 확인 true 로 하게되면 안잠겨있다는뜻
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //계정비밀번호가 만료 되지않았는지 확인 true 로 하게되면 만료가 되지않음
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 사용가능한 계정인지 아닌지 확인  true 로 하게되면 사용가능하다는뜻
    @Override
    public boolean isEnabled() {
        return true;
    }
    //모든 계정의 관한 목록을 리턴해 주는 함수
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
}
