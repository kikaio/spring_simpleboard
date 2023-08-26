package com.example.simpleboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simpleboard.dto.JoinFormDto;
import com.example.simpleboard.service.MemberService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;


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
    public ResponseEntity<String> postMethodName(@RequestBody JoinFormDto dto) 
    {
        log.info(
            "sign-up-process called, email[%s] plane pw[%s]"
                .formatted(dto.getEmail(), dto.getPassword())
        );

        memberService.signUp(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok().body("sign up success");
    }

    @GetMapping("/sign-in")
    public String memberSignIn()
    {
        log.info("memberSignIn called");
        return "sign_in";
    }

}
