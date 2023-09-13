package com.example.simpleboard.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SimpleBoardController 
{
    @GetMapping("/")
    public String index(Principal principal, Model model)
    {
        log.info("index called");
        if(principal != null)
        {
            String username = principal.getName();
            if(username.equals("anonymousUser") == false)
                model.addAttribute("username", username);
        }
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

    @GetMapping("/test-authentication")
    public ResponseEntity<String> testAuthentication(Principal principal)
    {
        if(principal == null)
        {
            return new ResponseEntity<String>("ok", HttpStatus.OK);
        }
        return new ResponseEntity<String>("ok", HttpStatus.OK);
    }

    @GetMapping("/test-authorization")
    public ResponseEntity<String> testAuthorization()
    {
        return new ResponseEntity<String>("ok", HttpStatus.OK);
    }
}
