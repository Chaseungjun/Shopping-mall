package com.example.shop2.domain.dto;

import com.example.shop2.domain.constant.ItemSellStatus;
import com.example.shop2.domain.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class ItemDto {

    private String itemNm;

    private Integer price;

    private String sellStatCd;

    private int stockNumber;

    private String itemDetail;


    public ItemDto of(String itemNm, int price, String sellStatCd, int stockNumber, String itemDetail) {
        return new ItemDto(itemNm, price, sellStatCd, stockNumber, itemDetail);
    }

}
