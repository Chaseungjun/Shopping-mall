package com.example.shop2.domain.service;

import com.example.shop2.domain.constant.ItemSellStatus;
import com.example.shop2.domain.dto.CartItemDto;
import com.example.shop2.domain.entity.CartItem;
import com.example.shop2.domain.entity.Item;
import com.example.shop2.domain.entity.Member;
import com.example.shop2.domain.repository.CartItemRepository;
import com.example.shop2.domain.repository.CartRepository;
import com.example.shop2.domain.repository.ItemRepository;
import com.example.shop2.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired private final CartService cartService;
    @Autowired private final ItemRepository itemRepository;
    @Autowired private final MemberRepository memberRepository;
    @Autowired private final CartItemRepository cartItemRepository;

    public CartServiceTest(@Autowired CartService cartService,
                           @Autowired ItemRepository itemRepository,
                           @Autowired MemberRepository memberRepository,
                           @Autowired CartItemRepository cartItemRepository) {
        this.cartService = cartService;
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Member createMember(){
        Member member = Member.builder().email("aaa@aaa.aaa").build();
        return memberRepository.save(member);
    }

    public Item createItem(){
        Item item = Item.of("itemNm", 1000, 10, "itemDetail", ItemSellStatus.SELL);
        return itemRepository.save(item);
    }

    @Test
    @DisplayName("addCart_test")
    void addCartTest() throws Exception {
        //given
        Item item = createItem();
        Member member = createMember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setItemId(item.getId());
        cartItemDto.setCount(1);

        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
        //when
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new); // 장바구니에 해당 상품이 있는지 조회

        //then
        assertEquals(item.getId(), cartItem.getItem().getId());
        assertEquals(cartItemDto.getCount(), cartItem.getCount());
    }
}