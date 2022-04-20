package dto.requestDto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeRequestDto {

    @Schema(description =  "리부 ID")
    private Long reviewId;
}
