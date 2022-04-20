package com.marketkurly.dto.requsetDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {
    @Schema(description = "회원가입 할 이메일", example = "user@mail.com")
    String email;
    @Schema(description = "회원가입 할 패스워드", example = "asdf1234")
    String password;
    @Schema(description = "회원가입 할 이름", example = "user")
    String username;

}
