package com.example.simpleboard.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String testAuthentication(Principal principal)
    {
        String message = "";
        if(principal == null)
        {
            message = "user not sign in state";
        }
        else
            message = "user sign in state";
        return "test_authentication";
    }

    @GetMapping("/test-authorization")
    public String testAuthorization(@RequestParam(required = false) String authname, Authentication auth, Model model)
    {
        String message = "";
        if(auth == null || auth.getPrincipal() == null)
            message = "check authentication[%s] failed. user not sign in state".formatted(authname);
        else
        {
            if(authname == null || authname.equals(""))
                message = "please url request param set.";

            var authorizations = auth.getAuthorities();
            for(var ele :authorizations)
            {
                // todo : check
            }
        }
        model.addAttribute("message", message);
        return "test_authorization";
    }
}
