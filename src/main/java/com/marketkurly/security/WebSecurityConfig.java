package com.marketkurly.security;

import com.marketkurly.exception.ExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // WebSecurityConfigurerAdapter를 상속받은 클래스에 @EnableWebSecurity 어노테이션을 사용하면
    // SpringSecurityFilterChain이 자동으로 포함되며, 기본적인 Web보안을 활성화하겠다는 의미입니다.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // authenticationManager를 Bean 등록합니다.
        // Spring Boot 2.x 부터는 자동으로 등록되지 않습니다.
        // 따라서 자동으로 등록이 되지 않기 때문에 외부로 노출해주는 메서드를 강제로 호출하여 @Bean으로 등록해주어야 합니다.
        // Override 하는 메서드는 authenticationManagerBean() 메서드이지 authenticationManager() 메서드가 아닙니다.
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        log.info("55555 : WebSecurityConfig.configure");
        web
                .ignoring()
                .antMatchers("/h2-console/**");
//                .antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
        //                .antMatchers("/api/**", "/configuration/ui",
//                        "/swagger-resources", "/configuration/security",
//                        "/swagger-ui.html", "/webjars/**","/swagger/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("66666 : WebSecurityConfig.configure");

        http
                .cors()
                .and()
                .headers().frameOptions().disable()

                .and()
                .httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제하겠습니다.
                .csrf().disable() //rest api이므로 csrf 보안이 필요없으므로 disable처리합니다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 토큰 기반 인증이므로 세션 역시 사용하지 않습니다.
                .and()
                .authorizeRequests()
                // 요청에 대한 사용권한 체크
                .anyRequest().permitAll()
                // 그외 나머지 요청은 누구나 접근 가능
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .permitAll()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);
                // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다

    }
}
