package com.example.shop2.domain.repository;

import com.example.shop2.domain.dto.ItemSearchDto;
import com.example.shop2.domain.dto.MainItemDto;
import com.example.shop2.domain.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    /*
    상품 검색키워드을 담고 있는 ItemSearchDto 객체와 페이징 정보를 담고있는 Pageable 객체를 파라미터로 받는
    메서드를 정의
    */

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
