package com.example.shop2.domain.entity;

import com.example.shop2.domain.constant.Role;
import com.example.shop2.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    public Member createMember() {
        Member member = Member.builder()
                .email("aaa@aaa.aaa")
                .address("test")
                .role(Role.USER)
                .name("test")
                .password("adsasdasdasd")
                .build();
        return memberRepository.save(member);
    }

        @Test
        @DisplayName("Auditing_test")
        @WithMockUser(username = "aaaa@aaa.aaa", roles = "USER") // 해당 계정으로 로그인 한 상태라고 가정
        void auditingTest () throws Exception {
            //given
            Member member = createMember();

            em.flush();
            em.clear();
            //when
            memberRepository.findById(member.getMemberId()).orElseThrow(EntityNotFoundException::new);

            //then
            System.out.println("register time" + member.getRegTime());
        }

    }
