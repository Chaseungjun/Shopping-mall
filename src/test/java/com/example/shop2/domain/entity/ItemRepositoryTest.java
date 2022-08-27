package com.example.shop2.domain.entity;

import com.example.shop2.domain.constant.ItemSellStatus;
import com.example.shop2.domain.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
@ExtendWith(SpringExtension.class)
class ItemRepositoryTest {


    @Autowired
    private ItemRepository itemRepository;


//    public ItemRepositoryTest(@Autowired ItemRepository itemRepository, @Autowired Item item, @Autowired ItemDto itemDto) {
//        this.itemRepository = itemRepository;
//        this.item = item;
//        this.itemDto = itemDto;
//    }


    private Item createItem() {

            Item item = Item.builder()
                    .itemNm("itemNm")
                    .itemDetail("itemDetail")
                    .price(1000)
                    .stockNumber(100)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .build();
            return item;
    }

    @Test
    @DisplayName("save test")
    void saveTest() throws Exception {
        //given
        Item item = createItem();
        //when
        Item savedItem = itemRepository.save(item);

        //then
        assertThat(item).isEqualTo(savedItem);
    }


    @Test
    @DisplayName("findByItemNm")
    void findByItemNm() throws Exception {
        //given
        Item item = createItem();
        itemRepository.save(item);
        //when
        Item findItem = itemRepository.findByItemNm("itemNm");
        //then
        assertThat(findItem.getItemNm()).isEqualTo("itemNm");
    }




}