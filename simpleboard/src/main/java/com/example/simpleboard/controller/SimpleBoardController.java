package com.example.simpleboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SimpleBoardController 
{
    @GetMapping("/")
    public String index()
    {
        log.info("index called");
        return "index";
    }

    @GetMapping("/view/dashboard")
    public String dashboard()
    {
        log.info("dashboard called");
        return "index";
    }
}
