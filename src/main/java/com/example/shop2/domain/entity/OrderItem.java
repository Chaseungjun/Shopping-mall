package com.example.shop2.domain.entity;


import com.example.shop2.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_Id")
    private Long orderItemId;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    @Builder
    public OrderItem(Item item, Order order, int orderPrice, int count) {
        this.item = item;
        this.order = order;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public void removeAndSetOrderAndOrderItem(Order order) {
        if (this.order != null) {
            this.order.getOrderItems().remove(this);
        }
        this.order = order;
        order.getOrderItems().add(this);
    }

    public static OrderItem createOrderItem(Item item, int count){  // 주문상품 생성
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    public void cancel(){
        this.getItem().addStock(count);  // 주문상품의 주문을 취소하면 그 주문수량만큼 상품의 재고를 더해줌
    }

    public int getTotalPrice(){
        return orderPrice * count;   // 주문상품의 총 가격 (주문상품 x 주문상품의 개수)
    }
}