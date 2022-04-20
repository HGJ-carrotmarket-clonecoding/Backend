package com.marketkurly.dto.responseDto;

import com.marketkurly.model.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResDto {
    String token;
    User user;
}
