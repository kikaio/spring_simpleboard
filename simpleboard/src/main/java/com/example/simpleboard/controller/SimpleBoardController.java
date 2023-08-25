package com.example.simpleboard.controller;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SimpleBoardController 
{
    
    @GetMapping("/")
    public String index()
    {
        log.info("index called");
        return "index";
    }

    @GetMapping("/view/login")
    public String login()
    {
        log.info("login called");
        return "";
    }
}
