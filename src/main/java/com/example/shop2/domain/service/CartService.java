package com.example.shop2.domain.service;

import com.example.shop2.domain.dto.CartItemDto;
import com.example.shop2.domain.entity.Cart;
import com.example.shop2.domain.entity.CartItem;
import com.example.shop2.domain.entity.Item;
import com.example.shop2.domain.entity.Member;
import com.example.shop2.domain.repository.CartItemRepository;
import com.example.shop2.domain.repository.CartRepository;
import com.example.shop2.domain.repository.ItemRepository;
import com.example.shop2.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email){
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        //장바구니에 담을 상품조회
        Member member = memberRepository.findByEmail(email); // 현재 로그인한 회원 조회

        Cart cart = cartRepository.findByMemberId(member.getId()); // 회원의 장바구니가 있는지 조회

        if (cart == null){  // 상품을 처음으로 장바구니에 담을 경우 장바구니가 없으므로 생성
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
        // 장바구니에 해당 상품이 있는지 조회
        if (savedCartItem != null){
            savedCartItem.addCartItem(cartItemDto.getCount()); // 장바구니에 상품이 있는 경우 수량을 더해줌
            return savedCartItem.getCartItemId();
        }else {
            CartItem cartItem = CartItem.createCartItem(item, cart, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getCartItemId();
            // 없는 경우 장바구니상품을 만들고 저장
        }

    }
}
