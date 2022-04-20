package com.marketkurly.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @Schema(description = "회원 이메일")
    String email;

    @Schema(description = "회원 비밀번호")
    String password;
}
