package com.example.shop2.domain.service;

import com.example.shop2.domain.dto.ItemFormDto;
import com.example.shop2.domain.dto.ItemImgDto;
import com.example.shop2.domain.dto.ItemSearchDto;
import com.example.shop2.domain.dto.MainItemDto;
import com.example.shop2.domain.entity.Item;
import com.example.shop2.domain.entity.ItemImg;
import com.example.shop2.domain.repository.ItemImgRepository;
import com.example.shop2.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if (i == 0)
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){          // 등록된 상품을 불러오는 메서드
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        } // ItemImg를 화면으로 불러올 때, Dto로 변환

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);  //item을 화면으로 불러올 때, Dto로 변환

        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;

    }

    //상품 수정
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws IOException {

        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        //상품 엔티티조회
        item.updateItem(itemFormDto);  // 화면으로부터 전달받은 ItemFormDto를 통해 상품 엔티티를 업데이트

        List<Long> itemImgIds = itemFormDto.getItemImgIds();  // 상품 이미지 아이디 리스트 조회
        for (int i=0; i<itemImgIds.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
            // 이미지를 업데이트 하기위해서 이미지아이디와 이미지파일정보를 전달
        }
        return item.getId();
    }

    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto,pageable);
    }

    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}