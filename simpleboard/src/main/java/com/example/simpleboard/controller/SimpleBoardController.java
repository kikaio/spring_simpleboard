package com.example.simpleboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/status")
    public ResponseEntity<String> status()
    {
        log.info("status ok");
        return new ResponseEntity<String>("ok", HttpStatus.OK);
    }
}
