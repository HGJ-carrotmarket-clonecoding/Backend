package com.marketkurly.controller;


import com.marketkurly.dto.requsetDto.SignupRequestDto;
import com.marketkurly.dto.responseDto.ResponseDto;
import com.marketkurly.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "User Controller Api V1")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping("/register")
    public ResponseDto createUser(@RequestBody SignupRequestDto signupRequestDto){
        log.info("POST, '/user/register', email={}, password={}, username={}",
                signupRequestDto.getEmail(), signupRequestDto.getPassword(), signupRequestDto.getUsername());
        String result = "failed";
        String msg = "회원가입에 실패하였습니다.";
        if (userService.registerUser(
                signupRequestDto.getEmail(),
                signupRequestDto.getPassword(),
                signupRequestDto.getUsername())) {
            result = "success";
            msg = "성공적으로 회원가입되었습니다.";
        }
        return new ResponseDto(result, msg, "");
    }


}
