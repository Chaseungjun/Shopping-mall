package com.example.shop2.domain.repository;

import com.example.shop2.domain.dto.CartDetailDto;
import com.example.shop2.domain.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepositoryCustom {


    CartItem findByCartIdAndItemId(Long cartId, Long itemId);


}
