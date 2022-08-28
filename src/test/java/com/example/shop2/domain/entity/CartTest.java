package com.example.shop2.domain.entity;

import com.example.shop2.domain.dto.MemberFormDto;
import com.example.shop2.domain.repository.CartRepository;
import com.example.shop2.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    private Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setAddress("address");
        memberFormDto.setEmail("aaa@aaa.com");
        memberFormDto.setName("name");
        memberFormDto.setPassword("1111");

        Member createdMember = Member.createMember(memberFormDto, passwordEncoder);

        return createdMember;
    }

    @Test
    @DisplayName("장바구니 화원 매핑 조회 테스트")
    void findCartAndMemberTest() throws Exception {
        //given
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);
        //when
        em.flush();
        em.clear();

        Cart savedCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);
        //then
        assertEquals(savedCart.getMember().getId() , member.getId());

    }

}