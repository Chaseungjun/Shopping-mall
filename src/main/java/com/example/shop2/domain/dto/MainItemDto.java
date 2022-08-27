package com.example.shop2.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {

    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail, Integer price, String imgUrl) {
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.price = price;
        this.imgUrl = imgUrl;
    }
}
