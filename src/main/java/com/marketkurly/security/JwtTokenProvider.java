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

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    // JwtProvier 클래스는 토큰을 생성하고 해당 토큰이 유효한지 또는 토큰에서 인증 정보를 조회하는 역할을 담당합니다.

//    @Value("${jwt.key}")
    private String secretKey = "spring-boot-jwt-market-kurly-secret-key-spring-boot-jwt-market-kurly-secret-key";


    // 토큰 유효시간
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
        log.info("22222 : JwtTokenProvider.createToken");
        StringJoiner joiner = new StringJoiner(",");
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            joiner.add(authority);
        }
        String authorities = joiner.toString();

        Date now = new Date();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authorities) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(getSigninKey(), SignatureAlgorithm.HS256) // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact();

    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        log.info("============getAuthentication===========");
        log.info("username={}, password={}", userDetails.getUsername(), userDetails.getPassword());
        log.info("============getAuthentication===========");
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getSigninKey()).build();
        Jws<Claims> claims = parser.parseClaimsJws(token);
        return claims.getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옵니다. "bearer" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        final String header = request.getHeader("authorization");
        if (header != null && (header.toLowerCase().indexOf("bearer ") == 0))
            return header.substring(7);
        else return header;
    }

    // 토큰의 유효성 + 만료일자 확인
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
