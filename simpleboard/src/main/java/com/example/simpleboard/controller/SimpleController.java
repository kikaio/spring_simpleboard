package com.example.simpleboard.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
            log.info("auth user name[%s]".formatted(auth.getName()));
        return "index";
    }    

    @GetMapping("/about")
    public String about()
    {
        return "about";
    }
}
