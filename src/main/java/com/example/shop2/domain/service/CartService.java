package com.example.shop2.domain.service;

import com.example.shop2.domain.dto.CartDetailDto;
import com.example.shop2.domain.dto.CartItemDto;
import com.example.shop2.domain.dto.CartOrderDto;
import com.example.shop2.domain.dto.OrderDto;
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
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;

    private final OrderService orderService;

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
            return savedCartItem.getId();
        }else {
            CartItem cartItem = CartItem.createCartItem(item, cart, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
            // 없는 경우 장바구니상품을 만들고 저장
        }

    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){   // 장바구니 조회
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null){
            return cartDetailDtoList;
        }
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email){
        // 로그인 한 회원과 장바구니에 상품을 저장한 회원이 같은지 검사
        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember(); // 장바구니상품을 저장한 회원을 조회

        if (!StringUtils.equals(member.getEmail(), savedMember.getEmail())){
            return false;
        }
        return true;
    }
    public void updateCartItemCount(Long cartItemId, int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }
    public void deleteCartItem(Long cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email){
        List<OrderDto> orderDtoList = new ArrayList<>();  // 주문을 위한 Dto

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());  // 장바구니에 있는 상품의 아이디
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList, email);

        for (CartOrderDto cartOrderDto : cartOrderDtoList) { // 주문한 상품들을 장바구니에서 제거
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
        return orderId;
    }
}
