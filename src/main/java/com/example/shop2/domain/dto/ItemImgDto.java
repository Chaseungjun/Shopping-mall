package com.example.shop2.domain.dto;

import com.example.shop2.domain.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {

    private static ModelMapper modelMapper = new ModelMapper();
    // static으로 설정해서 ItemImgDto 객체를 생성하지 않아도 호출할 수 있도록 함

    private Long id;

    private String imgName; // 업데이트 할 이미지 파일명

    private String oriImgName; // 원본 이미지 파일명

    private String imgUrl; // 이미지 조회 경로

    private String repimgYn; // 대표이미지 여부

    public static ItemImgDto of(ItemImg itemImg){
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
