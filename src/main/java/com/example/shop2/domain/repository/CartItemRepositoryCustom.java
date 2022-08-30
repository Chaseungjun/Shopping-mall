package com.example.shop2.domain.repository;

import com.example.shop2.domain.dto.CartDetailDto;
import com.example.shop2.domain.entity.CartItem;

import java.util.List;

public interface CartItemRepositoryCustom {
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
