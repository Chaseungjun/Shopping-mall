package com.example.shop2.domain.controller;

import com.example.shop2.domain.dto.MemberFormDto;
import com.example.shop2.domain.entity.Member;
import com.example.shop2.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/members")
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/new")
    public String memberForm(@ModelAttribute MemberFormDto memberFormDto){
        return "member/memberForm";
    }

    @PostMapping("/new")
    public String newMember(@Valid @ModelAttribute MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        if (!StringUtils.hasText(memberFormDto.getName())){
            bindingResult.rejectValue("name", "required.memberFormDto.name");
        }
        if (!StringUtils.hasText(memberFormDto.getEmail())){
            bindingResult.rejectValue("email", "required.memberFormDto.email");
        }
        if (memberFormDto.getPassword().length() > 16 || memberFormDto.getPassword().length() < 8 ){
            bindingResult.rejectValue("password", "required.memberFormDto.password", new Object[]{8,16}, null);
        }
        if (!StringUtils.hasText(memberFormDto.getAddress())){
            bindingResult.rejectValue("address", "required.memberFormDto.address");
        }

        if (bindingResult.hasErrors()){
            log.info("error = {}", bindingResult);
            return "member/memberForm";
        }

        try{
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){
            log.info("errorMessages", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            //일단 중복되는 회원이 있는경우 뷰로 보여주려면 model객체가 필요함
            // 근데 @ModelAttribute랑 model을 같이 쓸 수 있는지 모르겠음
            return "member/memberForm";
        }

        return "redirect:/members/login";
    }

    @GetMapping("/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호가 잘못되었습니다");
        return "/member/memberLoginForm";
    }
}
