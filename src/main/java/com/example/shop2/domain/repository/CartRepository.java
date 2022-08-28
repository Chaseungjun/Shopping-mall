package com.example.shop2.domain.repository;

import com.example.shop2.domain.entity.Cart;
import com.example.shop2.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(Long memberId);

}
