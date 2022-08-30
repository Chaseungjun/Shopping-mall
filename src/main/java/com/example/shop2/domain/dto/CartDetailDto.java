package com.example.shop2.domain.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {

    private Long cartItemId;

    private String itemNm;

    private int count;

    private int price;

    private String imgUrl;

    @QueryProjection
    public CartDetailDto(Long cartItemId, String itemNm, int count, int price, String imgUrl) {
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.count = count;
        this.price = price;
        this.imgUrl = imgUrl;
    }
}
