package security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;


    //클라이언트에 요청이 들어오면 로그인 과 회원가입 을 할시 인증이 된 토큰이 맞는지 아닌지 확인 하는 과정 인증이 되어있는
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        log.info("====================token=================");
        log.info(token);
        log.info("====================token=================");
        //토큰이 있을경우 validateToken 에 토큰 값이 서명이 있는지 확인
        if (token != null && jwtTokenProvider.validateToken(token)) {
            //인증이 된 프로바이더 토큰 이면 인증된 이메일(아이디) 를 찾아서 서명 된 토큰 을 준비작업
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //필터체인 은  doFilter 로 모든 요청 과 응답 을  여기로 거쳐야 가능함 (로그인 이나 회원가입)
        chain.doFilter(request, response);
    }
}
