package com.example.shop2.domain.dto;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberFormDto {

    private String name;

    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;

    private String password;

    private String address;


}
