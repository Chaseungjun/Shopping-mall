package com.example.shop2.domain.service;

import com.example.shop2.domain.dto.OrderDto;
import com.example.shop2.domain.dto.OrderHistDto;
import com.example.shop2.domain.dto.OrderItemDto;
import com.example.shop2.domain.entity.*;
import com.example.shop2.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    private final ItemImgRepository itemImgRepository;

    public Long Order(OrderDto orderDto, String email){
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();

        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());

        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);
        return order.getOrderId();

    }
    @Transactional(readOnly = true)
    // 주문목록 리스트 -> 주문목록에서 주문상품의 이미지(경로) ->  주문상품을 주문내여에 추가 -> 주문내역을 주문내역리스트에 추가
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){
        List<Order> orders = orderRepository.findOrders(email, pageable);  // 유저의 주문목록 조회
        Long total = orderRepository.countOrder(email);  // 유저의 주문 총 개수 조회

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();  // 주문의 주문상품들
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y"); // 주문상품의 이미지
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, total);

    }
    @Transactional(readOnly = true)
    public Boolean validateOrder(Long orderId, String email){
        Member member = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member orderMember = order.getMember();
        // 현재 회원과 주문을 한 회원이 같은 사람인지 검증
        if (!StringUtils.equals(member, orderMember)){
            return false;
        }
        return true;
    }
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder(); //변경감지
    }

}
