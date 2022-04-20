package security;


import exception.InvalidTokenException;
import exception.TokenNullException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
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

//    @Value("${jwt.token.key}")

    private String secretKey;
    //토큰 발행 시간
    private long tokenValidTime = 120 * 60 * 1000L;
    //시큐리티 user 디테일서비스
    private  final UserDetailsServiceImpl userDetailsService;
//==================================================================
    @PostConstruct
    //시크릿 키 만드는 함수 자바 유틸 안에있는 Base64(비트반위) 로 바이트 단위 로 암호화 시키는 함수
    protected  void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    private Key getSigninKey(){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    //<1단계 >필터를 거쳐 인증 인 되었을때 실행하는 토큰생성 함수 (헤더값 만들기)
    public String createToken(Authentication authentication){
        StringJoiner joiner = new StringJoiner(",");
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()){
            String authority = grantedAuthority.getAuthority();
            joiner.add(authority);
        }
        //<2단계> 페이로드 값 만들기(데이터 값 만들기)
        String authorities = joiner.toString();
        Date now = new Date();
        return Jwts.builder()
                .setSubject(authentication.getName()) //  이름중 권한 권한 왁인
                .claim("authorities", authorities) // 인증
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+tokenValidTime)) //토큰유효일자
                .signWith(getSigninKey(), SignatureAlgorithm.HS256) // 토큰 을 반드는 알고리즘 방식
                .compact();
    }
    //===================================================================

    //서명을 하기위한 인증 절차 이메일(아이디) 로 토큰이 있는지 없는지 판단 후
    public  Authentication getAuthentication(String token){
        UserDetails userDetails =userDetailsService.loadUserByUsername(this.getUserPk(token));
        log.info("=========getAuthentication==================");
        log.info("username={}, password={}", userDetails.getUsername(),userDetails.getPassword());
        log.info("==========getAuthentication===============");
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    //<3단계> 서명 하기
    public  String getUserPk(String token){
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getSigninKey()).build();
        Jws<Claims>claims = parser.parseClaimsJws(token);
        return  claims.getBody().getSubject();
    }
    //클라이언트 에서 요청이 들어오면 인증된 헤더 값으로 확인 절차 함수
    public  String resolveToken(HttpServletRequest request){
        final String header = request.getHeader("ahthorization");
        if (header != null && (header.toLowerCase().indexOf("bearer")==0))
            return header.substring(7);
        else return  header;
    }
    //예외 처리 함수
    //토큰이 발행시간 을 갱신해주는 함수 서명값으로 확인후 없으면 재발행 존재하지 않을수도 있기때문에 예외처리
    public boolean validateToken(String jwtToken){
        if(jwtToken == null){
            log.info("토큰이 존재하지 않습니다");
            throw  new TokenNullException("토큰이 존재하지 않습니다");
        }
        try{
            JwtParser parser = Jwts.parserBuilder().setSigningKey(getSigninKey()).build();
            Jws<Claims>claims = parser.parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            log.info("정상적인 토큰이 아닙니다");
            throw  new InvalidTokenException("정상적인 토큰이 아닙니다.");
        }
    }

}
