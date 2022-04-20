package com.marketkurly.controller;

import com.marketkurly.dto.reponseDto.LoginResDto;
import com.marketkurly.dto.reponseDto.ResponseDto;
import com.marketkurly.dto.requestDto.SignupRequestDto;
import com.marketkurly.dto.requestDto.UserRequestDto;
import com.marketkurly.exception.JwtTokenExpiredException;
import com.marketkurly.exception.UnauthenticatedException;
import com.marketkurly.model.User;
import com.marketkurly.security.JwtTokenProvider;
import com.marketkurly.security.UserDetailsImpl;
import com.marketkurly.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.AllArgsConstructor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "회원가입")
    @PostMapping("/register")
    public ResponseDto createUser(@RequestBody SignupRequestDto signupRequestDto) {
        log.info("POST, '/user/register', email={}, password={}, username={}",
                signupRequestDto.getEmail(),signupRequestDto.getPassword(),signupRequestDto.getUsername());
        String result = "failde";
        String msg = "회원가입에 실패하였습니다.";
        if (userService.registerUser(
                signupRequestDto.getEmail(),
                signupRequestDto.getPassword(),
                signupRequestDto.getUsername())) {
            result = "success";
            msg = "회원가입에 성공하였습니다.";

        }
        return new ResponseDto(result, msg,"");
    }

    @Operation(summary = "이메일 중복 확인")
    @PostMapping("/register")
    public ResponseDto dupCheckEmail(@Parameter(name="eamil",description = "이메일", in = ParameterIn.QUERY)@RequestBody String email){
        log.info("GET, '/user/register', email={}", email);
        String result = "failde";
        String msg = "사용할 수 없는 아이디 입니다";
        if(userService.checkDupEmail(email)){
            result = "success";
            msg = "사용가능한 아이디 입니다.";
        }
        return new ResponseDto(result,msg,"");
    }
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseDto login(@RequestBody UserRequestDto userRequestDto){
        log.info("POST, '/user/login', email={}, password={}",userRequestDto.getEmail(),userRequestDto.getPassword());
        User user = userService.loginValidCheck(userRequestDto.getEmail(),userRequestDto.getPassword());
        String token = userService.createToken(userRequestDto.getEmail(),userRequestDto.getPassword());
        LoginResDto loginResDto = LoginResDto.builder().token(token).user(user).build();
    return new ResponseDto("success","로그인에 성공하였습니다",loginResDto);
    }

    @Operation(summary = "로그인 확인")
    @GetMapping("/info")
    public ResponseDto getUserInfoFromToken(@RequestHeader(value = "authorization")String token){
        log.info("GET, '/user/info', token={}", token);
        if(jwtTokenProvider.validateToken(token)){
            return new ResponseDto("success","유저정보를 성공적으로 불러왔습니다",getLoginResDtoFromToken(token));
        }else {
            throw new JwtTokenExpiredException("토큰이 만료되었습니다");
        }
    }
    public LoginResDto getLoginResDtoFromToken(String token){
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        Object principal = authentication.getPrincipal(); //현재 로그인한 유저 가져오기
        if(principal instanceof UserDetailsImpl){
            return getLoginResDtoFromPrincipal((UserDetailsImpl)principal,token);
        }else{
            log.info("유효하지 않은 토큰입니다");
            throw new UnauthenticatedException("유효하지 않은 토큰입니다.");
        }
    }

    public LoginResDto getLoginResDtoFromPrincipal(UserDetailsImpl principal,String token){
        User user =principal.getUser();
        return LoginResDto.builder().token(token).user(user).build();
    }
}