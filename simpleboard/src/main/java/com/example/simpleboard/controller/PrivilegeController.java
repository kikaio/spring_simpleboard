package com.example.simpleboard.controller;

import java.util.ArrayList;

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
import com.example.simpleboard.service.PrivilegeService;

@Controller
@RequestMapping("/privileges")
public class PrivilegeController {
    
    private final PrivilegeService privilegeService;

    public PrivilegeController(PrivilegeService privilegeService)
    {
        this.privilegeService = privilegeService;
    }

    @GetMapping("")
    public String getAllPrivileges(Model model)
    {
        var privileges = privilegeService.getAllPrivileges();
        var privilegeDtos = new ArrayList<PrivilegeDto>();
        privileges.forEach(entity->{
            privilegeDtos.add(new PrivilegeDto(entity));
        });
        model.addAttribute("privileges",privilegeDtos);
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
