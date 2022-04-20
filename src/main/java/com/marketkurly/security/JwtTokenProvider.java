package com.marketkurly.security;

import com.marketkurly.exception.InvalidTokenException;
import com.marketkurly.exception.TokenNullException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j // 로깅에 대한 추상 레이어를 제공하는 인터페이스의 모음
@RequiredArgsConstructor //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
@Component // 직접 작성한 Class를 Bean으로 등록하기 위한 어노테이션
public class JwtTokenProvider {

    @Value("${jwt.token.key}") // properties 파일에 세팅한 내용을 Spring 변수에 주입하는 역할
    private String secretKey;

    // 토큰 유효시간 120분
    private long tokenValidTime = 120 * 60 * 1000L;
    private final UserDetailsServiceImpl userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    private Key getSigninKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰 생성
    public String createToken(Authentication authentication) {
        StringJoiner joiner = new StringJoiner(",");
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            joiner.add(authority);
        }
        String authorities = joiner.toString();

        Date now = new Date();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authorities)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        log.info("============getAuthentication===========");
        log.info("username={}, password={}", userDetails.getUsername(), userDetails.getPassword());
        log.info("============getAuthentication===========");
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPk(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getSigninKey()).build();
        Jws<Claims> claims = parser.parseClaimsJws(token);
        return claims.getBody().getSubject();
    }


    public String resolveToken(HttpServletRequest request) {
        final String header = request.getHeader("authorization");
        if (header != null && (header.toLowerCase().indexOf("bearer ") == 0))
            return header.substring(7);
        else return header;
    }

    public boolean validateToken(String jwtToken) {
        if (jwtToken == null) {
            log.info("토큰이 존재하지 않습니다.");
            throw new TokenNullException("토큰이 존재하지 않습니다.");
        }
        try {
            JwtParser parser = Jwts.parserBuilder().setSigningKey(getSigninKey()).build();
            Jws<Claims> claims = parser.parseClaimsJws(jwtToken);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            log.info("정상적인 토큰이 아닙니다.");
            throw new InvalidTokenException("정상적인 토큰이 아닙니다.");
        }
    }
}