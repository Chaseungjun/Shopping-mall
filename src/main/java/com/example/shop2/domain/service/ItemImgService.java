package com.example.shop2.domain.service;

import com.example.shop2.domain.entity.ItemImg;
import com.example.shop2.domain.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService { // 상품 이미지를 로컬에 업로드하고, 해당 이미지를 폴더에 저장하기 위한 서비스

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    // 이미지파일 저장을 위한 메서드
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        // 사용자가 이미지를 등록했다면 업로드했던 이미지파일의 원래이름으로 MultipartFile에 파일 정보가 담기게 됨
        String imgName = "";
        String imgUrl="";

        //로컬에 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            /*
            oriImgName이 존재한다면 이미지를 등록한 것이므로 uploadFile 메서드를 호출하여, 파일을 업로드하고
            그 이름을 imgName 변수에 저장
             */

            imgUrl = "/images/item/" + imgName;  // 컨트롤러에서 @RequestParam으로 받음?

            /*
            로컬에 저장한 이미지를 불러올 경로를 설정, WebMvcConfig에서 "/images/**"를 설정해주었고
            itemImgLocation의 경로를 shop/item으로 설정하였으므러 상품이미지를 불러오는 경로로
            "/images/item/"를 설정한다.
             */
        }

        itemImg.updateItemImg(imgName, oriImgName, imgUrl);
        itemImgRepository.save(itemImg);
    }



    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws IOException {
        if (!itemImgFile.isEmpty()){  // 수정한 상품이미지가 있는지 조회(이미지가 수정되면 MultipartFile에 자동으로? 들어감)
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);
            // 있다면 기존에 저장된 이미지를 조회

            if (!StringUtils.isEmpty(savedItemImg.getImgName())){  // 기존의 이미지파일 삭제
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            String originalImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, originalImgName, itemImgFile.getBytes());// 업데이트한 상품 이미지 파일을 업로드
            String imgUrl = "/images/item/" + imgName;

            savedItemImg.updateItemImg(originalImgName, imgName, imgUrl);  // 변경된 상품 이미지정보 세팅
        }

    }
}
