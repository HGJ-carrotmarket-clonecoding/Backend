package dto.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CartReponseDto {

    private List<ProductResponseDto> products;
    private Long itemCount;

    public CartReponseDto(List<ProductResponseDto> responseDtoList, Long itemCount){
        this.products = responseDtoList;
        this.itemCount = itemCount;
    }
}
