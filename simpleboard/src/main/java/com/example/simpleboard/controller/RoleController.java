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

import com.example.simpleboard.dto.RoleDto;
import com.example.simpleboard.service.RoleService;

@Controller
@RequestMapping("/roles")
public class RoleController {
    
    private final RoleService roleService;

    public RoleController(RoleService roleService)
    {
        this.roleService = roleService;
    }

    @GetMapping("")
    public String getRoles(Model model)
    {
        var roles = roleService.getAllRoles();
        var roleDtos = new ArrayList<RoleDto>();
        roles.forEach(ele->{
            var dto = new RoleDto(ele);
            roleDtos.add(dto);
        });

        model.addAttribute("roles", roleDtos);
        return "/roles/roles";
    }

    @GetMapping("/{id}")
    public String getRole(@PathVariable(name="id", required = true) long id, Model model)
    {
        var role = roleService.getRole(id);
        if(role == null)
        {
            //todo : error page
            return "";
        }
        var roleDto = new RoleDto(role);
        //todo : privileges 추가 할 것.
        model.addAttribute("role", roleDto);
        return "/roles/role";
    }

    @GetMapping("/create")
    public String createRolePage()
    {
        return "/roles/role_create";
    }

    @PostMapping("/create")
    public String createRole(@ModelAttribute RoleDto roleDto)
    {
        var newRole = roleDto.toEntity();
        //todo : valid check, duplicated check
        if(roleService.createRole(newRole) == false)
        {
            //todo : error page
            return "";
        }
        return "redirect:/roles";
    }

    @GetMapping("/update")
    public String updateRolePage(@RequestParam(name="id", required = true)long id, Model model)
    {
        var role = roleService.getRole(id);
        if(role == null)
        {
            //todo : error page
            return "";
        }
        var roleDto = new RoleDto(role);
        model.addAttribute("role", roleDto);
        return "/roles/role_update";
    }

    @PostMapping("/{id}")
    public String updateRole(@PathVariable(name="id", required = true)long id, @ModelAttribute RoleDto roleDto)
    {
        if(roleDto.getId() != id)
        {
            //todo : error page
            return "";
        }
        var newRole = roleDto.toEntity();
        if(roleService.updateRole(newRole) == false)
        {
            //todo : error page
            return "";
        }

        return "redirect:/roles/%d".formatted(id);
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteRole(@PathVariable(name="id", required = true)long id)
    {
        if(roleService.deleteRole(id) == false)
        {
            //todo : error page
            return new ModelAndView("");
        }

        var modelAndView = new ModelAndView("redirect:/roles");
        modelAndView.setStatus(HttpStatus.SEE_OTHER);
        return modelAndView;
    }
}
