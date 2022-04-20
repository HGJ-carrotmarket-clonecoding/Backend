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
import java.util.Locale;
import java.util.StringJoiner;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${jwt.token.key}")
    private String secretkey;

    // 토큰 유효시간 120분
    private long tokenVaildTime = 120 * 60 * 1000L;

    private final UserdetailsServiceImpl userdetailsService;

    @PostConstruct // 객체 초기화, secretKey를 Base64로 인코딩한다
    protected void init(){ secretkey = Base64.getEncoder().encodeToString(secretkey.getBytes());
    }

    private Key getSigninKey(){
        byte[] keyBytes = secretkey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰 생성
    public String createToken(Authentication authentication){
        StringJoiner joiner = new StringJoiner(",");
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            joiner.add(authority);
        }
        String authorities = joiner.toString();
        Date now =new Date();

        return Jwts.builder()
                .setSubject(authentication.getName()) //토큰제목 - 토큰에서 사용자에 식별 값
                .claim("authorities",authorities) //JWT의 'body', JWT 생성자가 JWT 수신자가 확인하기를 바라는 정보들을 담고 있다
                .setIssuedAt(now) //Jwt 발급한 시간 지정
                .setExpiration(new Date(now.getTime()+tokenVaildTime)) // 만료시간 지정
                .signWith(getSigninKey(), SignatureAlgorithm.ES256) //ES256알고리즘에 적합한 키를 이용하여 서명
                .compact(); //압축
    }

    // 인증 성공시 SecurityContextHolder에 저장할 Authentication 객체 생성
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userdetailsService.loadUserByUsername(this.getUserPk(token));
        log.info("============getAuthentication===========");
        log.info("username={}, password={}", userDetails.getUsername(), userDetails.getPassword());
        log.info("============getAuthentication===========");
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    //Jwt Token에서 User PK 추출
    //토큰은 String 형태 생성 -> 우리가 사용하기 위한 형태로 parsing하기 위해서 jwts.parse()사용
    public String getUserPk(String token){
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

    // Jwt Token의 유효성 및 만료 기간 검사
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
