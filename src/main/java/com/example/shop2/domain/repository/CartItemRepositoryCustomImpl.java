package com.example.shop2.domain.repository;

import com.example.shop2.domain.dto.CartDetailDto;
import com.example.shop2.domain.dto.QCartDetailDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.shop2.domain.entity.QCartItem.*;
import static com.example.shop2.domain.entity.QItem.*;
import static com.example.shop2.domain.entity.QItemImg.*;

public class CartItemRepositoryCustomImpl implements CartItemRepositoryCustom{


    public CartItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    JPAQueryFactory queryFactory;


    @Override
    public List<CartDetailDto> findCartDetailDtoList(Long cartId) {
        List<CartDetailDto> result = queryFactory
                .select(
                        new QCartDetailDto(
                        cartItem.id,
                        item.itemNm,
                        cartItem.count,
                        item.price,
                        itemImg.repimgYn
                ))
                .from(cartItem, itemImg)
                .join(cartItem.item, item)
                .where(cartItem.cart.id.eq(cartId))
                .where(itemImg.item.eq(cartItem.item))
                .where(itemImg.repimgYn.eq("Y"))
                .orderBy(cartItem.regTime.desc())
                .fetch();
        return result;

    }
}
