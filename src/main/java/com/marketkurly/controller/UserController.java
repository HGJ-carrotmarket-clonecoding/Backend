package com.marketkurly.controller;

import com.marketkurly.dto.reponseDto.LoginResDto;
import com.marketkurly.dto.reponseDto.ResponseDto;
import com.marketkurly.dto.requestDto.SignupRequestDto;
import com.marketkurly.dto.requestDto.UserRequestDto;
import com.marketkurly.model.User;
import com.marketkurly.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.AllArgsConstructor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

}