package com.example.shop2.domain.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.shop2.domain.dto.QCartDetailDto is a Querydsl Projection type for CartDetailDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCartDetailDto extends ConstructorExpression<CartDetailDto> {

    private static final long serialVersionUID = 2027547576L;

    public QCartDetailDto(com.querydsl.core.types.Expression<Long> cartItemId, com.querydsl.core.types.Expression<String> itemNm, com.querydsl.core.types.Expression<Integer> count, com.querydsl.core.types.Expression<Integer> price, com.querydsl.core.types.Expression<String> imgUrl) {
        super(CartDetailDto.class, new Class<?>[]{long.class, String.class, int.class, int.class, String.class}, cartItemId, itemNm, count, price, imgUrl);
    }

}

