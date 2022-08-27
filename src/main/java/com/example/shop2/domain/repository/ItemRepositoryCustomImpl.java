package com.example.shop2.domain.repository;

import com.example.shop2.domain.constant.ItemSellStatus;
import com.example.shop2.domain.dto.ItemSearchDto;
import com.example.shop2.domain.dto.MainItemDto;
import com.example.shop2.domain.dto.QMainItemDto;
import com.example.shop2.domain.entity.Item;
import com.example.shop2.domain.entity.QItem;
import com.example.shop2.domain.entity.QItemImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.shop2.domain.entity.QItem.*;
import static com.example.shop2.domain.entity.QItemImg.*;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory; //동적으로 쿼리를 생성하기 위해 필요



    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);  //생성자로 EntityManager를 넣어줌줌
   }

   private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : item.itemSellStatus.eq(searchSellStatus);
        //조건이 null이라서 null이 반환되면 해당 검색조건은 무시된다.
   }

   private BooleanExpression regDtsAfter(String searchDateType){
       LocalDateTime dateTime = LocalDateTime.now();

       if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
           return null;
       }else if (StringUtils.equals("1d", searchDateType)){
           dateTime = dateTime.minusDays(1);  // 시간을 현재부터 1일전으로 세팅
       }else if (StringUtils.equals("1w", searchDateType)){
           dateTime = dateTime.minusWeeks(1);
       }else if (StringUtils.equals("1m", searchDateType)){
           dateTime = dateTime.minusMonths(1);
       }else if (StringUtils.equals("6m", searchDateType)){
           dateTime = dateTime.minusMonths(6);
       }
       return item.regTime.after(LocalDate.from(dateTime));  // 해당 시간 이후로 등록된 상품
   }

   private BooleanExpression searchByLike(String searchBy, String searchQuery){ //검색조건, 검색어
        if (StringUtils.equals("itemNm", searchBy)){
            return item.itemNm.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy" , searchBy)) {
            return item.createdBy.like("%" + searchQuery + "%");
        }
        return null;
   }

//   private BooleanExpression itemNmLike(String searchQuery){
//        return StringUtils.isEmpty(searchQuery) ? null : item.itemNm.like("%" + searchQuery + "%");
//        // 검색어가 null이 아니면 해당 검색어로 조회
//   }

   private BooleanExpression itemNmLike(String searchQuery){
        if (StringUtils.equals("itemNm", searchQuery)){
            return item.itemNm.like("%" + searchQuery + "%");
            // 검색어가 null이 아니면 해당 검색어로 조회
        }
       return null;
   }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<Item> content = queryFactory.selectFrom(item)
                .where(searchSellStatusEq(itemSearchDto.getSearchSellStatus()))
                .where(regDtsAfter(itemSearchDto.getSearchDateType()))
                .where(searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())  // 데이터를 가지고 올 시작 인덱스를 지정
                .limit(pageable.getPageSize())   // 한 번에 가지고 올 최대 개수 지정
                .fetch();

        Long total = queryFactory.select(Wildcard.count).from(item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        List<MainItemDto> content = queryFactory.select(
                        new QMainItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                item.price,
                                itemImg.imgUrl
                        )
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();


        return new PageImpl<>(content, pageable, total);

    }
}
