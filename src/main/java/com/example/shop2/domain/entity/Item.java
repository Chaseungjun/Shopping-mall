package com.example.shop2.domain.entity;

import com.example.shop2.domain.BaseTimeEntity;
import com.example.shop2.domain.constant.ItemSellStatus;
import com.example.shop2.domain.dto.ItemFormDto;
import com.example.shop2.exception.OutOfStockException;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Table(indexes = {
        @Index(columnList = "itemNm")
})
@RequiredArgsConstructor
@Entity
public class Item extends BaseTimeEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "item_id")
   private Long id;

    @Column(length = 20)
    private String itemNm;

    @Column(length = 10)
    private int price;


    @Column(length = 10)
    private int stockNumber;


    @Column(length = 500)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @Builder
    public Item (String itemNm, int price, int stockNumber, String itemDetail, ItemSellStatus itemSellStatus) {
        this.itemNm = itemNm;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
    }

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber){
       int restStock =  this.stockNumber - stockNumber;
       if (restStock < 0){
           throw new OutOfStockException("상품의 재고가 부족합니다.");
       }
       this.stockNumber = restStock;
    }
    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }


    public static Item of(String itemNm, int price, int stockNumber, String itemDetail, ItemSellStatus itemSellStatus){
        return new Item(itemNm, price, stockNumber, itemDetail, itemSellStatus);
    }

    public void changeItemNm(String itemNm) {
        this.itemNm = itemNm;
    }

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeStockNumber(int stockNumber) {
        this.stockNumber = stockNumber;
    }

    public void changeItemDetail(String itemDetail) {
        this.itemDetail = itemDetail;
    }

    public void changeItemSellStatus(ItemSellStatus itemSellStatus) {
        this.itemSellStatus = itemSellStatus;
    }
}
