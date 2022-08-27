package com.example.shop2.domain.dto;

import com.example.shop2.domain.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    private String itemNm;
    private int count;
    private int orderPrice;
    private String imgUrl; // 상품이미지의 경로

    public OrderItemDto(OrderItem orderItem, String imgUrl) {   // OrderItem 객체의 값을 생성자로 받음
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
