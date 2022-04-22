package com.marketkurly.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Schema(name = "상품 정보", description = "상품 아아디, 이름, 가격, 설명, 유닛, 배송, 분류, 이미지, 타입, 조회 수, 리뷰 정보 Domain Class")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 아아디")
    private Long id;

    @Column
    @Schema(description = "상품 이름")
    private String name;

    @Column
    @Schema(description = "상품 가격")
    private Long price;

    @Column
    @Schema(description = "상품 설명")
    private String description;

    @Column
    @Schema(description = "유닛")
    private int unit;

    @Column
    @Schema(description = "배송")
    private String delivery;

    @Column
    @Schema(description = "대분류")
    private String category1;

    @Column
    @Schema(description = "중분류")
    private String category2;

    @Column
    @Schema(description = "이미지")
    private String image;

    @Column
    @Schema(description = "타입")
    private String type;

    @Column
    @Schema(description = "조회수")
    private int viewCount;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @Schema(description = "리뷰")
    private List<Review> reviews;

    public Product(String name, Long price, String description, String category1, String category2, String image, int viewCount) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.unit = 1;
        this.type = "상온/종이포장";
        this.delivery = "샛별배송/택배배송";
        this.image = image;
        this.category1 = category1;
        this.category2 = category2;
        this.viewCount = viewCount;
    }

   /* public Product(ProductRequestDto productDto) {
        this.name = productDto.getTitle();
        this.price = productDto.getPrice();
        this.description = productDto.getDescription();
        this.unit = 1;
        this.type = "상온/종이포장";
        this.delivery = "샛별배송/택배배송";
        this.image = productDto.getImage();
        this.category1 = productDto.getCategory1();
        this.category2 = productDto.getCategory2();
        this.viewCount = 0;
    }*/
}
