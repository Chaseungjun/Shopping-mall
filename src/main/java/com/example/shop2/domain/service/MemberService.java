package com.example.shop2.domain.service;

import com.example.shop2.domain.constant.Role;
import com.example.shop2.domain.entity.Member;
import com.example.shop2.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService{

    private final MemberRepository memberRepository;

    public Long saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member).getMemberId();
    }

    public void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null){
            throw new UsernameNotFoundException("아이디 또는 비밀번호가 존재하지 않습니다");
        }
        return User.builder()
                .username(member.getName())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
