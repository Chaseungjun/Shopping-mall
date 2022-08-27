package com.example.shop2.domain.service;

import com.example.shop2.domain.dto.MemberFormDto;
import com.example.shop2.domain.entity.Member;
import com.example.shop2.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;


    private Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setAddress("address");
        memberFormDto.setEmail("aaa@aaa.com");
        memberFormDto.setName("name");
        memberFormDto.setPassword("1111");

        Member createdMember = Member.createMember(memberFormDto, passwordEncoder);

        return createdMember;
    }

    @Test
    @DisplayName("회원가입 테스트")
    void save_test() throws Exception {
        //given
        Member member = createMember();

        Long fakeMemberId = 1L;
        ReflectionTestUtils.setField(member, "memberId", fakeMemberId);
        // ReflectionTestUtils.setField(대상 객체, "변수명", 원하는 값);

        //mocking
        given(memberRepository.save(any())).willReturn(member);
        given(memberRepository.findById(fakeMemberId)).willReturn(Optional.ofNullable(member));

        //when
        Long newMemberId = memberService.saveMember(member);
        Member findMember = memberRepository.findById(newMemberId).get();

        //then
        assertThat(member.getMemberId()).isEqualTo(findMember.getMemberId());
    }

    @Test
    @DisplayName("중복회원가입_테스트")
    void duplicate_save_test() throws Exception {
        //given
        Member member1 = createMember();
        Member member2 = createMember();

        given(memberRepository.save(eq(member1))).willReturn(member1);
        given(memberRepository.save(eq(member2))).willThrow(IllegalStateException.class);
        //when
        memberService.saveMember(member1);


        //then
        assertThatThrownBy(() -> memberService.saveMember(member2), "이미 가입된 회원입니다.", IllegalStateException.class);
    }



}