package com.marketkurly.controller;


import com.marketkurly.dto.requsetDto.LoginResDto;
import com.marketkurly.dto.requsetDto.SignupRequestDto;
import com.marketkurly.dto.requsetDto.UserRequestDto;
import com.marketkurly.dto.responseDto.ResponseDto;
import com.marketkurly.exception.JwtTokenExpiredException;
import com.marketkurly.exception.UnauthenticatedException;
import com.marketkurly.model.User;
import com.marketkurly.security.JwtTokenProvider;
import com.marketkurly.security.UserDetailsImpl;
import com.marketkurly.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "User Controller Api V1")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

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

    @Operation(summary = "이메일 중복확인")
    @GetMapping("/register")
    public ResponseDto dupCheckEmail(@Parameter(name = "email", description = "이메일", in = ParameterIn.QUERY) @RequestParam String email) {
        log.info("GET, '/user/register', email={}", email);
        String result = "failed";
        String msg = "사용할 수 없는 아이디입니다.";
        if (userService.checkDupEmail(email)) {
            result = "success";
            msg = "사용가능한 아이디입니다.";
        }
        return new ResponseDto(result, msg, "");
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseDto login(@RequestBody UserRequestDto userRequestDto) {
        log.info("33333 : UserController.login");
        log.info("POST, '/user/login', email={}, password={}", userRequestDto.getEmail(), userRequestDto.getPassword());
        User user = userService.loginValidCheck(userRequestDto.getEmail(), userRequestDto.getPassword());
        String token = userService.createToken(userRequestDto.getEmail(), userRequestDto.getPassword());
        LoginResDto loginResDto = LoginResDto.builder().token(token).user(user).build();
        return new ResponseDto("success", "로그인에 성공하였습니다.", loginResDto);
    }

    @Operation(summary = "로그인 확인")
    @GetMapping("/info")
    public ResponseDto getUserInfoFromToken(@RequestHeader(value = "authorization") String token) {
        log.info("GET, '/user/info', token={}", token);
        if (jwtTokenProvider.validateToken(token)) {
            return new ResponseDto("success", "유저정보를 성공적으로 불러왔습니다.", getLoginResDtoFromToken(token));
        } else
            throw new JwtTokenExpiredException("토큰이 만료되었습니다.");
    }

    private LoginResDto getLoginResDtoFromToken(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return getLoginResDtoFromPrincipal((UserDetailsImpl) principal, token);
        } else {
            log.info("유효하지 않은 토큰입니다.");
            throw new UnauthenticatedException("유효하지 않은 토큰입니다.");
        }
    }

    private LoginResDto getLoginResDtoFromPrincipal(UserDetailsImpl principal, String token) {
        User user = principal.getUser();
        return LoginResDto.builder().token(token).user(user).build();
    }


}
