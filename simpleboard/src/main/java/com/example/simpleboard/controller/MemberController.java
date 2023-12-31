package com.example.simpleboard.controller;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.simpleboard.dto.MemberDto;
import com.example.simpleboard.dto.PrivilegeDto;
import com.example.simpleboard.dto.RoleDto;
import com.example.simpleboard.dto.SimplePageDto;
import com.example.simpleboard.entity.Member;
import com.example.simpleboard.service.RoleService;
import com.example.simpleboard.service.SimpleUserDetailsService;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/members")
public class MemberController 
{
    private final SimpleUserDetailsService simpleUserDetailsService;
    private final RoleService roleService;

    private static final int cntPerPage = 10;
    private static int testedMemberCnt = 0;
    public MemberController(SimpleUserDetailsService simpleUserDetailsService, RoleService roleService)
    {
        this.simpleUserDetailsService = simpleUserDetailsService;
        this.roleService = roleService;
    }

    @GetMapping("/test")
    public String test()
    {
        int testMemberCnt = 27;
        for(int idx = 0; idx < testMemberCnt; ++idx)
        {
            Member admin = Member.builder()
                .email("test_member_%d".formatted(idx+1 + testedMemberCnt))
                .password("1234")
                .build()
            ;
            simpleUserDetailsService.createMember(admin.getEmail(), admin.getPassword());
        }
        testedMemberCnt += testMemberCnt;
        
        return "redirect:/members";
    }

    @GetMapping("")
    public String getMembers(
        @RequestParam(name="page", defaultValue = "0") int pageIdx
        , Model model
    )
    {
        var pageable = PageRequest.of(pageIdx, cntPerPage);
        var members = simpleUserDetailsService.getMembers(pageable);
        var pageMemberDtos = members.map(entity-> new MemberDto(entity));
        var pageDto = new SimplePageDto<>(pageMemberDtos);
        model.addAttribute("members", pageMemberDtos.getContent());
        model.addAttribute("pageDto", pageDto);
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
        ArrayList<RoleDto> roleDtos = new ArrayList<>();
        ArrayList<PrivilegeDto> privDtos = new ArrayList<>();
        var roles = member.getRoles();
        for(var role : roles)
        {
            roleDtos.add(new RoleDto(role));
            var privileges = role.getPrivileges();
            for(var priv : privileges)
            {
                privDtos.add(new PrivilegeDto(priv));
            }
        }

        model.addAttribute("member", memberDto);
        model.addAttribute("roles", roleDtos);
        model.addAttribute("privileges", privDtos);
        return "/members/member";
    }

    @GetMapping("/create")
    public String createMemberPage()
    {
        //임의로 admin이 유저 정보 삽입 시 사용되는 API
        //todo : authorization check (ROLE_ADMIN)
        return "/members/member_create";
    }

    @PostMapping("/create")
    public String createMember(@ModelAttribute MemberDto dto)
    {
        //임의로 admin이 유저 정보 삽입 시 사용되는 API
        //todo : authorization check (ROLE_ADMIN)
        var entity = dto.toEntity();
        //최초 권한은 아예 없는 걸로?
        //valid check
        if(simpleUserDetailsService.validCheckCreateMember(entity) == false)
        {
            //todo : error page로 보낼 것.
            return "";
        }
        if(simpleUserDetailsService.createMember(entity.getEmail(), entity.getPassword()) == false)
        {
            //todo : error page로 보낼 것.
            return "";
        }
        return "redirect:/members";
    }
    
    @GetMapping("/update")
    public String updateMemberPage(@RequestParam(name = "id", required = true) long id, Model model)
    {
        //todo : authorization check (ROLE_ADMIN 또는 자신에 대한 수정 요청)
        //role에 대한 변경 등은 별도 API로 분리할 것.
        var member = simpleUserDetailsService.getMember(id);
        if(member == null)
        {
            //todo : error page로 보낼 것.
            return "";
        }
        var memberDto = new MemberDto(member);
        model.addAttribute("member", memberDto);
        return "/members/member_update";
    }

    @PostMapping("/{id}")
    public String updateMember(@PathVariable(name="id", required = true) long id, @ModelAttribute MemberDto dto)
    {
        //todo : authorization check (ROLE_ADMIN 또는 자신에 대한 수정 요청)
        //현재까지는 password 변경정도?
        var newMem = dto.toEntity();
        if(newMem.getId() != id)
        {
            //todo : error page 처리
            return "";
        }
        if(simpleUserDetailsService.updateOnlyMember(newMem) == false)
        {
            //todo : error page 처리
            return "";
        }
        return "redirect:/members/%d".formatted(id);
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteMember(@PathVariable(name="id", required = true) long id)
    {
        //todo : 본인 또는 ADMIN만 가능하도록 authorization check.
        if(simpleUserDetailsService.deleteMember(id) == false)
        {
            //todo : error pgae 처리
            return new ModelAndView("");
        }
        //todo : delete member
        var retView = new ModelAndView("redirect:/members");
        retView.setStatus(HttpStatus.SEE_OTHER);
        return retView;
    }

    @PostMapping("/{id}/roles/{roleId}")
    public ModelAndView postRole(@PathVariable(name="id", required = true) long id, @PathVariable(name="roleId", required = true) long roleId)
    {
        var member = simpleUserDetailsService.getMember(id);
        if(member == null)
        {
            // todo : error 처리
            return new ModelAndView("");
        }

        var role = roleService.getRole(roleId);
        if(role == null)
        {
            // todo : error 처리
            return new ModelAndView("");
        }
        var roles = new HashSet<>(member.getRoles());
        if(roles.contains(role))
        {
            //todo : duplicate error
            return new ModelAndView("");
        }
        roles.add(role);
        simpleUserDetailsService.updateOnlyRoles(member, roles);
        var retView = new ModelAndView("redirect:/members/%d".formatted(id), HttpStatus.SEE_OTHER);
        return retView;
    }

    @DeleteMapping("{id}/roles/{roleId}")
    public ModelAndView deleteRole(@PathVariable(name="id", required = true)long id, @PathVariable(name="roleId", required = true)long roleId)
    {
        var member = simpleUserDetailsService.getMember(id);
        if(member == null)
        {
            // todo : error 처리
            return new ModelAndView("");
        }

        var role = roleService.getRole(roleId);
        if(role == null)
        {
            // todo : error 처리
            return new ModelAndView("");
        }
        var roles = new HashSet<>(member.getRoles());
        if(roles.remove(role) == false)
        {
            //todo : duplicate error
            return new ModelAndView("");
        }
        simpleUserDetailsService.updateOnlyRoles(member, roles);
        var modelView = new ModelAndView("", HttpStatus.SEE_OTHER);
        return modelView;
    }
}
