package dto.responseDto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResponseDto {
    @Schema(description = "응답 결과")
    private String result;

    @Schema(description = "응답 메세지")
    private String message;

    @Schema(description = "응답 데이터")
    private Object data;
}
