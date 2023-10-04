package com.example.simpleboard.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.simpleboard.dto.MemberDto;
import com.example.simpleboard.service.SimpleUserDetailsService;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/members")
public class MemberController 
{
    private final SimpleUserDetailsService simpleUserDetailsService;

    public MemberController(SimpleUserDetailsService simpleUserDetailsService)
    {
        this.simpleUserDetailsService = simpleUserDetailsService;
    }

    @GetMapping("")
    public String getMembers(Model model)
    {
        var members = simpleUserDetailsService.getMembers();
        var memberDtos = new ArrayList<MemberDto>();
        members.forEach(ele->{
            memberDtos.add(new MemberDto(ele));
        });
        model.addAttribute("members", memberDtos);
        return "/members/members";
    }

    @GetMapping("/{id}")
    public String getMember(@PathVariable(required = true) long id, Model model)
    {
        var member = simpleUserDetailsService.getMember(id);
        if(member == null)
        {
            //todo : send error page
            return "";
        }
        var memberDto = new MemberDto(member);
        model.addAttribute("member", memberDto);
        return "/members/member";
    }

    @GetMapping("/create")
    public String createMemberPage()
    {
        //임의로 admin이 유저 정보 삽입 시 사용되는 API
        //todo : authorization check 

        return "/members/member_create";
    }

    @PostMapping("/create")
    public String createMember(@ModelAttribute MemberDto dto)
    {
        //임의로 admin이 유저 정보 삽입 시 사용되는 API
        //todo : authorization check 
        var entity = dto.toEntity();
        //최초 권한은 아예 없는 걸로?
        if(simpleUserDetailsService.createMember(entity))
        {
            //todo : error page로 보낼 것.
            return "";
        }
        return "redirect:/members";
    }
    
    @GetMapping("/update")
    public String updateMemberPage()
    {
        return "";
    }

    @PostMapping("/update")
    public String updateMember()
    {
        return "";
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteMember()
    {
        //todo : delete member
        var retView = new ModelAndView("redirect:/members");
        retView.setStatus(HttpStatus.SEE_OTHER);
        return retView;
    }

}
