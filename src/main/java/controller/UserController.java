package controller;


import domain.User;
import dto.requestDto.SignupRequestDto;
import dto.requestDto.UserRequestDto;
import dto.responseDto.LoginResDto;
import dto.responseDto.ResponseDto;
import exception.JwtTokenExpiredException;
import exception.UnauthenticatedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import security.JwtTokenProvider;
import security.UserDetailsImpl;
import service.UserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "User Controller Api V1")
@RequestMapping("/user")
public class UserController {

    //주입
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Operation(summary = "회원가입")
    @PostMapping("/register")
    public ResponseDto createUser(@RequestBody SignupRequestDto signupRequestDto){
        log.info("POST , 'user/register' , email={}, password={}, username={}",
                signupRequestDto.getEmail(),signupRequestDto.getPassword(),signupRequestDto.getUsername());
        String result = "failed";
        String msg = "회원가입에 실패하였습니다";
        if (userService.registerUser(
                signupRequestDto.getEmail(),
                signupRequestDto.getPassword(),
                signupRequestDto.getPassword()
        )){
            result = "success";
            msg = "성공적으로 회원가입이 되었습니다";
        }
        return new ResponseDto(result, msg, "");
    }
    //로그인
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseDto login(@RequestBody UserRequestDto userRequestDto) {
        log.info("POST, '/user/login', email={}, password={}", userRequestDto.getEmail(), userRequestDto.getPassword());
        User user = userService.loginValidCheck(userRequestDto.getEmail(), userRequestDto.getPassword());
        String token = userService.createToken(userRequestDto.getEmail(), userRequestDto.getPassword());
        LoginResDto loginResDto = LoginResDto.builder().token(token).user(user).build();
        return new ResponseDto("success", "로그인에 성공하였습니다.", loginResDto);
    }
    //로그인 확인
    @Operation(summary = "로그인 확인")
    @GetMapping("/info")
    public ResponseDto getUserInfoFromToken(@RequestHeader(value = "authorization") String token) {
        log.info("GET, '/user/info', token={}", token);
        if (jwtTokenProvider.validateToken(token)) {
            return new ResponseDto("success", "유저정보를 성공적으로 불러왔습니다.", getLoginResDtoFromToken(token));
        } else
            throw new JwtTokenExpiredException("토큰이 만료되었습니다.");
    }
    //유효한 토큰인지 확인
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
    //UserDetailsImpl 에서 유저 확인 후 토큰값 과 유저 정보 반환환
   private LoginResDto getLoginResDtoFromPrincipal(UserDetailsImpl principal, String token) {
        User user = principal.getUser();
        return LoginResDto.builder().token(token).user(user).build();
    }

}
