package com.example.simpleboard.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simpleboard.dto.MemberFormDto;
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
    
    @PostMapping(value="/sign-up-process")
    public String memberSignUpProcess(@ModelAttribute MemberFormDto dto) 
    {
        log.info(
            "sign-up-process called, email[%s] plane pw[%s]"
                .formatted(dto.getEmail(), dto.getPassword())
        );

        boolean signUpSuccess = memberService.signUp(dto.getEmail(), dto.getPassword());
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

    @GetMapping("list")
    public String getAllMembers(Model model)
    {
        var members = memberService.getAllMembers();
        var membersDto = new ArrayList<MemberFormDto>();
        for(var mem : members)
        {
            var newMemDto = new MemberFormDto(mem.getEmail(), mem.getPassword());
            membersDto.add(newMemDto);
        }
        model.addAttribute("members", membersDto);
        return "/member/members";
    }
}
