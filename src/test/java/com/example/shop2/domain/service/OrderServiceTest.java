package com.example.shop2.domain.service;

import com.example.shop2.domain.constant.ItemSellStatus;
import com.example.shop2.domain.constant.OrderStatus;
import com.example.shop2.domain.dto.OrderDto;
import com.example.shop2.domain.entity.Item;
import com.example.shop2.domain.entity.Member;
import com.example.shop2.domain.entity.Order;
import com.example.shop2.domain.entity.OrderItem;
import com.example.shop2.domain.repository.ItemRepository;
import com.example.shop2.domain.repository.MemberRepository;
import com.example.shop2.domain.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class OrderServiceTest {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    private final OrderService orderService;

    public OrderServiceTest(@Autowired MemberRepository memberRepository,
                            @Autowired OrderRepository orderRepository,
                            @Autowired ItemRepository itemRepository,
                            @Autowired OrderService orderService) {
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.orderService = orderService;
    }

    public Member createMember(){
        Member member = Member.builder().email("test@aaa.aaa").build();
        memberRepository.save(member);
        return member;
    }
    public Item createItem(){
        Item item = Item.builder()
                .itemNm("itemNm")
                .itemDetail("detail")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(10)
                .price(1000)
                .build();
        itemRepository.save(item);
        return item;
    }

    @Test
    @DisplayName("Order_test")
    void orderTest() throws Exception {
        //given
        Item item = createItem();
        Member member = createMember();
        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(item.getId());
        orderDto.setCount(10);
        //when
        Long order = orderService.Order(orderDto, member.getEmail());
        Order savedOrder = orderRepository.findById(order).orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = savedOrder.getOrderItems();
        int totalPrice = orderDto.getCount() * item.getPrice();  // 주문된 수량 * 해당 상품의 가격
       //then
        assertEquals(totalPrice, savedOrder.getTotalPrice());
    }

    @Test
    @DisplayName("cancelOrder")
    void cancelOrder() throws Exception {
        //given
        Member member = createMember();
        Item item = createItem();

        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(item.getId());
        orderDto.setCount(10);

        Long orderId = orderService.Order(orderDto, member.getEmail());
        //when
        orderService.validateOrder(orderId, member.getEmail());
        orderService.cancelOrder(orderId);

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        //then
        assertEquals(OrderStatus.CANCEL,order.getOrderStatus());
        assertEquals(10, item.getStockNumber());

    }

}