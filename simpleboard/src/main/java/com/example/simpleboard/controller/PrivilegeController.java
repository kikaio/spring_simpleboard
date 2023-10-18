package com.example.simpleboard.controller;

import java.util.ArrayList;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.simpleboard.dto.PrivilegeDto;
import com.example.simpleboard.dto.SimplePageDto;
import com.example.simpleboard.service.PrivilegeService;

@Controller
@RequestMapping("/privileges")
public class PrivilegeController {
    
    private final PrivilegeService privilegeService;
    private static final int cntPerPage = 3;

    private static int curTestCnt = 0;

    public PrivilegeController(PrivilegeService privilegeService)
    {
        this.privilegeService = privilegeService;
    }

    @GetMapping("/test")
    public String test()
    {
        int testLoopCnt = 13;
        for(int idx = 0; idx < testLoopCnt; idx++)
        {
            privilegeService.createPrivilege(
                new PrivilegeDto(
                    null, "privilege_name_%d".formatted(idx+curTestCnt), "desc for privilege_%d".formatted(idx+curTestCnt)
                ).toEntity()
            );
        }
        curTestCnt+= testLoopCnt;
        return "redirect:/privileges";
    }

    @GetMapping("")
    public String getAllPrivileges(
        @RequestParam(name="page", defaultValue = "0")int pageIdx
        , Model model
    )
    {
        var pageable = PageRequest.of(pageIdx, cntPerPage);

        var privileges = privilegeService.getAllPrivileges(pageable);
        var privilegeDtos = privileges.map(entity-> new PrivilegeDto(entity));
        var pageDto = new SimplePageDto<>(privilegeDtos);

        model.addAttribute("privileges",privilegeDtos);
        model.addAttribute("pageDto", pageDto);
        return "/privileges/privileges";
    }

    @GetMapping("/{id}")
    public String getPrivilege(@PathVariable(name="id", required = true) long id, Model model)
    {
        var privilege = privilegeService.getPrivilege(id);
        if(privilege == null)
        {
            //todo : error page
            return "";
        }
        var privilegeDto = new PrivilegeDto(privilege);
        model.addAttribute("privilege", privilegeDto);
        return "/privileges/privilege";
    }

    @GetMapping("/create")
    public String createPrivilegePage()
    {
        return "/privileges/privilege_create";
    }

    @PostMapping("/create")
    public String createPrivilege(@ModelAttribute PrivilegeDto dto)
    {
        var entity = dto.toEntity();
        if(privilegeService.createPrivilege(entity) == false)
        {
            //todo : error page
            return "";
        }
        return "redirect:/privileges";
    }

    @GetMapping("/update")
    public String updatePrivilegePage(@RequestParam(name="id", required = true) long id, Model model)
    {
        var entity = privilegeService.getPrivilege(id);
        if(entity == null)
        {
            //todo : error page
            return "";
        }
        var privilegeDto = new PrivilegeDto(entity);
        model.addAttribute("privilege", privilegeDto);
        return "/privileges/privilege_update";
    }

    @PostMapping("/{id}")
    public String updatePrivilege(@PathVariable(name="id", required = true) long id, @ModelAttribute PrivilegeDto dto)
    {
        var newEntity = dto.toEntity();
        if(dto.getId() != id)
        {
            //todo : error page
            return "";
        }
        if(privilegeService.updatePrivilege(newEntity) == false)
        {
            //todo : error page
            return "";
        }
        return "redirect:/privileges";
    }

    @DeleteMapping("/{id}")
    public ModelAndView deletePrivilege(@PathVariable(name="id", required = true) long id)
    {
        if(privilegeService.deletePrivilege(id) == false)
        {
            //todo : error page
            return new ModelAndView("");
        }

        var modelAndView = new ModelAndView("redirect:/privileges");
        modelAndView.setStatus(HttpStatus.SEE_OTHER);
        return modelAndView;
    }

}
