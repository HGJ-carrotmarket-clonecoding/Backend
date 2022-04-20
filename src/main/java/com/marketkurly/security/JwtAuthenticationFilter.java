package com.marketkurly.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {


    private final JwtTokenProvider jwtTokenProvider;

    public void doFilter(ServletRequest request, ServletResponse reponse, FilterChain chain) throws IOException, ServletException{
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        log.info("====================token=================");
        log.info(token);
        log.info("====================token=================");
        if(token != null && jwtTokenProvider.validateToken(token)){ //토큰 검증
            Authentication authentication = jwtTokenProvider.getAuthentication(token); //인증객체생성
            SecurityContextHolder.getContext().setAuthentication(authentication); //SecurityContextHolder에 인증 객체 저장
        }
        chain.doFilter(request,reponse);
    }
}
