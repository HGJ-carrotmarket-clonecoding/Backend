package dto.requestDto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDto {
    @Schema(description = "제품 ID")
    @NotNull(message = "상품을 선택해주세요")
    private Long productId;
    @Schema(description = "장바구니 추가 수량")
    @NotNull(message = "수량을 입력해주세요")
    private Long amount;

}
