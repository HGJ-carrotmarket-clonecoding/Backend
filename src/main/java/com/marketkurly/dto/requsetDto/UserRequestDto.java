package com.marketkurly.dto.requsetDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @Schema(description = "회원 이메일", example = "user@mail.com")
    private String email;
    @Schema(description = "회원 비밀번호", example = "asdf1234")
    private String password;
}
