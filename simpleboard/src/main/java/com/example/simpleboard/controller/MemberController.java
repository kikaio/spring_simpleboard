package com.example.simpleboard.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.simpleboard.dto.MemberDto;
import com.example.simpleboard.dto.MemberFormDto;
import com.example.simpleboard.entity.MemberEntity;
import com.example.simpleboard.service.MemberService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController 
{
    @Autowired
    private MemberService memberService;

    @GetMapping("/sign-up")
    public String memberSignUp()
    {
        log.info("sign-up called");
        return "sign_up";
    }
    
    @PostMapping("/sign-up-process")
    public String memberSignUpProcess(@ModelAttribute MemberFormDto dto) 
    {
        log.info(
            "sign-up-process called, email[%s] plane pw[%s]"
                .formatted(dto.getEmail(), dto.getPassword())
        );

        boolean signUpSuccess = memberService.signUp(dto.getEmail(), dto.getPassword(), "NONE");
        if(signUpSuccess == false)
        {
            //page 를 다르게
        }
        else
        {
            //page 를 다르게
        }
        return "redirect:/";
    }

    @GetMapping("/sign-in")
    public String memberSignIn()
    {
        log.info("memberSignIn called");
        return "sign_in";
    }

    @GetMapping("/sign-in-success")
    public String memberSignInSuccess()
    {
        log.info("memberSignIn Success called");
        return "sign_in_success";
    }

    @GetMapping("/list")
    public String getAllMembers(Model model)
    {
        var members = memberService.getAllMembers();
        var membersDto = new ArrayList<MemberDto>();
        for(var mem : members)
        {
            var newMemDto = MemberDto.builder()
                .email(mem.getEmail())
                .role(mem.getRole())
                .isLocked(mem.isLocked())
                .build();
            membersDto.add(newMemDto);
        }
        model.addAttribute("members", membersDto);
        return "/member/members";
    }

    @GetMapping("/lock")
    public String memberSetLock(
        @RequestParam(name="email", required = true) String email
        , @RequestParam(name="dolock", required = true) boolean isLock
        , RedirectAttributes redirectAttr
        )
    {
        Optional<MemberEntity> target = memberService.findMemberByEmail(email);
        if(target.isPresent() == false)
        {
            //present invalid request page
            redirectAttr.addFlashAttribute("return_url", "/");
            return "";
        }
        MemberEntity member = target.get();
        if(member.isLocked() != !isLock)
        {
            //lock flag sync failed;
            //present invalid request page
            redirectAttr.addFlashAttribute("return_url", "/");
            return "";
        }
        else
        {
            if(isLock)
                member.doLock();
            else
                member.doUnlock();
        }
        memberService.saveMember(member);
        redirectAttr.addFlashAttribute("changed_member", member);
        redirectAttr.addFlashAttribute("changed_lock_status", true);
        
        return "redirect:/member/list";
    }
}
