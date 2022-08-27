package com.example.shop2.domain.entity;

import com.example.shop2.domain.BaseTimeEntity;
import com.example.shop2.domain.constant.Role;
import com.example.shop2.domain.dto.MemberFormDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long memberId;


    @Column(unique = true)
    private String email;

    private String password;

    @Column(length = 5)
    private String name;


    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String email, String password, String name, String address, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.role = role;
    }

    @OneToMany(mappedBy = "member")
    private List<Order> order = new ArrayList<>();

    public static Member of(String email, String password, String name, String address, Role role) {
        return new Member(email, password, name, address, role);
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        return Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .password(passwordEncoder.encode(memberFormDto.getPassword()))
                .address(memberFormDto.getAddress())
                .role(Role.ADMIN)
                .build();
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeAddress(String address) {
        this.address = address;
    }

    public void changeRole(Role role) {
        this.role = role;
    }


}
