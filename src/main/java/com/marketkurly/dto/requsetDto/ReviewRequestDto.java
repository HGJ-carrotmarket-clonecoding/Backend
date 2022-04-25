package com.marketkurly.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {
    @Schema(description = "상품 ID")
    @NotNull(message = "상품을 선택해주세요.")
    private Long productId;
    @Schema(description = "리뷰 제목")
    @NotNull(message = "제목을 입력해주세요.")
    private String title;
    @Schema(description = "리뷰 내용")
    @NotNull(message = "내용을 작성해주세요.")
    private String content;
}

