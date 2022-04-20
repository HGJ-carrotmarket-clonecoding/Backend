package dto.responseDto;


import domain.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResDto {

    String token;
    User user;
}
