package com.example.simpleboard.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class SimpleController 
{
    @GetMapping()
    public String index(Authentication auth)
    {
        if(auth != null)
        {
            log.info("auth user name[%s]".formatted(auth.getName()));
        }

        return "index";
    }    

    @GetMapping("/about")
    public String about()
    {
        return "about";
    }

    @GetMapping("/error")
    public String errorPage(
        @RequestParam(name="error", required = false, defaultValue = "unknown error") String errorMsg
        , Model model
    )
    {
        model.addAttribute("error", errorMsg);
        return "/layouts/error";
    }
}
