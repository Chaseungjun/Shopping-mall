package com.example.shop2.domain.entity;

import com.example.shop2.domain.constant.ItemSellStatus;
import com.example.shop2.domain.constant.Role;
import com.example.shop2.domain.repository.ItemRepository;
import com.example.shop2.domain.repository.MemberRepository;
import com.example.shop2.domain.repository.OrderItemRepository;
import com.example.shop2.domain.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class OrderTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @PersistenceContext
    private EntityManager em;


    public Order createOrder() {
        Member member = createMember();
        Item item = createItem();

        Order order = new Order();
        order.setMember(member);

        OrderItem orderItem = createOrderItem(item);
        orderItem.removeAndSetOrderAndOrderItem(order);

        return orderRepository.save(order);

    }

    public Member createMember() {
        Member member = Member.builder()
                .email("aaa@aaa.aaa")
                .address("test")
                .role(Role.USER)
                .name("test")
                .password("adsasdasdasd")
                .build();
        return memberRepository.save(member);
    }


    @Test
    @DisplayName("cascade_test")
    void cascadeTest() throws Exception {
        //given
        Order order = new Order();

        Item item = createItem();


        OrderItem orderItem = createOrderItem(item);
        orderItem.removeAndSetOrderAndOrderItem(order);

        //when
        orderRepository.saveAndFlush(order);

        em.clear();

        Order savedOrder = orderRepository.findById(order.getOrderId()).orElseThrow(EntityExistsException::new);

        //then
        assertEquals(1, savedOrder.getOrderItems().size());
    }

    private OrderItem createOrderItem(Item item) {
        OrderItem orderItem = OrderItem.builder()
                .orderPrice(1000)
                .count(10)
                .item(item)
                .build();
        return orderItem;
    }

    private Item createItem() {
            Item item = Item.builder()
                    .itemNm("test")
                    .price(1000)
                    .itemDetail("detail")
                    .stockNumber(10)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .build();
       return itemRepository.save(item);
    }


        @Test
        @DisplayName("orphanRemovalTest")
        void orphanRemovalTest () throws Exception {
            //given
            Order order = new Order();

            Member member = createMember();
            memberRepository.save(member);
            order.setMember(member);

            Item item = createItem();

            OrderItem orderItem = createOrderItem(item);
            orderItem.removeAndSetOrderAndOrderItem(order);

            orderRepository.save(order);

            //when
            order.getOrderItems().remove(0);
            em.flush();

            //then
        }


        @Test
        @DisplayName("LazyLoading_test")
        void lazyLoadingTest () throws Exception {
            //given
            Order order = createOrder();
            Long orderItemId = order.getOrderItems().get(0).getOrderItemId();
            // 주문된 주문상품 리스트 중 0번째 인덱스의 주문상품 아이디를 가져옴
            em.flush();
            em.clear();

            //when
            OrderItem orderItem = orderItemRepository.findById(orderItemId)
                    .orElseThrow(EntityNotFoundException::new);

            //then
        }
    }

