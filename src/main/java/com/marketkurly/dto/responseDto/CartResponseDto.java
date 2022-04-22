package com.marketkurly.dto.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CartResponseDto {
    private List<ProductResponseDto> products;
    private Long itemCount;

    public CartResponseDto(List<ProductResponseDto> responseDtoList, Long itemCount) {
        this.products = responseDtoList;
        this.itemCount = itemCount;
    }
}