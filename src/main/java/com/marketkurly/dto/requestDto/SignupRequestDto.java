package com.marketkurly.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {
    @Schema(description = "회원가입 할 이메일",example = "user@mail.com")
    String email;
    @Schema(description = "회원가입 할 비밀번호",example = "asdf1234")
    String password;
    @Schema(description = "회원가입 할 이름",example = "user")
    String username;
}
