package com.example.simpleboard.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.simpleboard.dto.MemberDto;
import com.example.simpleboard.service.SimpleUserDetailsService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/sign/")
@Slf4j
public class SignController 
{
    private final SimpleUserDetailsService simpleUserDetailsService;

    public SignController(SimpleUserDetailsService simpleUserDetailsService)
    {
        this.simpleUserDetailsService = simpleUserDetailsService;
    }

    @GetMapping(value="/test")
    public String test() 
    {
        String email = "admin@admin.com";
        String pw_plane = "1234";
        var pwEncoder = new BCryptPasswordEncoder();
        String pw_encoded = pwEncoder.encode(pw_plane);
        var memberDto = new MemberDto(null, email, pw_encoded, true, false, false);
        var member = memberDto.toEntity();
        simpleUserDetailsService.createMember(member);
        return "redirect:/";
    }
    

    @GetMapping("sign-up")
    public String signUpPage(Model model)
    {
        model.addAttribute("isSignUpNow", true);
        return "sign/sign_up";
    }

    @GetMapping("sign-up-failed")
    public String signUpFailedPage()
    {
        return "sign/sign_up_failed";
    }

    @GetMapping("sign-up-success")
    public String signUpSuccessPage()
    {
        return "sign/sign_up_success";
    }

    @PostMapping("sign-up-process")
    public String signUpProcess(MemberDto memberDto, RedirectAttributes reAttr)
    {
        //todo : add validcheck logic

        var target = simpleUserDetailsService.getMember(memberDto.getEmail());
        if(target != null)
        {
            //sign up failed
            reAttr.addFlashAttribute("errorMsg", "failed : already member exist");
            return "redirect:/sign/sign-up-failed";
        }
        log.info("sign up password : %s".formatted(memberDto.getPassword()));
        var passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        log.info("sign up password : %s".formatted(memberDto.getPassword()));
        var newMember = memberDto.toEntity();
        if(simpleUserDetailsService.validCheckCreateMember(newMember) == false)
        {
            reAttr.addFlashAttribute("errorMsg", "failed : member data invalid. please check again");
            return "redirect:/sign/sign-up-failed";
        }
        
        if(simpleUserDetailsService.createMember(newMember) == false)
        {
            reAttr.addFlashAttribute("errorMsg", "failed : member data save failed. please check again");
            return "redirect:/sign/sign-up-failed";
        }
        
        return "redirect:/sign/sign-up-success";
    }



    @GetMapping("sign-in")
    public String signInPage(Model model)
    {
        model.addAttribute("isSignInNow", true);
        return "sign/sign_in";
    }

    @GetMapping("sign-in-success")
    public String signInSuccessPage()
    {
        return "sign/sign_in_success";
    }

    @GetMapping("sign-in-failed")
    public String signInFailedPage()
    {
        return "sign/sign_in_failed";
    }

    //실제  form login 의 요청을 처리하는 곳.
//    @PostMapping("sign-in-process")
    public String signInProcess(MemberDto tryMember, RedirectAttributes reAttr)
    {
        String email = tryMember.getEmail();
        log.info("password : %s".formatted(tryMember.getPassword()));
        String password = tryMember.getPassword();
        try {
            var userDetails = simpleUserDetailsService.loadUserByUsername(email);
            log.info("user details password : %s".formatted(userDetails.getPassword()));
            if(userDetails == null || simpleUserDetailsService.passwordCheck(password, userDetails.getPassword()) == false)
            {
                reAttr.addFlashAttribute("errorMge", "not exist user or invalid password. check again please");
                return "redirect:/sign/sign-in-failed";
            }
            return "redirect:/sign/sign-in-success";
                
        } catch (Exception e) {

            // TODO: handle exception
            reAttr.addFlashAttribute("errorMge", "not exist user or invalid password. check again please");
            return "redirect:/sign/sign-in-failed";
        }
    }
}
