package com.example.shop2.domain.service;


import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    //리소스 저장을 위한 메서드
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws IOException {
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        // 파일의 원래이름의 확장자만 잘라내서 uuid 값과 결합 -> 로컬 컴퓨터에 업로드할 파일이름 완성

        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        //-> ///C:/shop/ 경로에 savedFileName 이름으로 리소스 저장
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }

    public void deleteFile(String filePath){
        File deleteFile = new File(filePath); //파일이 저장된 경로를 이용하여 해당경로에 파일이 존재하면 파일객체를 생성

        if (deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
