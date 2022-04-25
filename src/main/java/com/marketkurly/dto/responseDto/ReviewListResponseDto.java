package com.marketkurly.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewListResponseDto {
    private List<ReviewResponseDto> reviews;
    private Long reviewCount;
    private int totalPage;
    private int nowPage;

    public ReviewListResponseDto(List<ReviewResponseDto> reviews,Long reviewCount,int page,int display){
        this.reviews = reviews;
        this.reviewCount = reviewCount;
        this.totalPage = page;
        this.nowPage = display;
    }
}
