package com.example.simpleboard.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.simpleboard.entity.MemberEntity;
import com.example.simpleboard.repositoty.MemberRepositoty;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MemberService {

    @Autowired
    private MemberRepositoty memberRepository;

    @Autowired
    private PasswordEncoder pwEncoder;

    @PostConstruct
    public void init()
    {
        String initEmail = "admin@admin.com";
        String initPw = "1234";
        boolean success = signUp(initEmail, initPw, "ADMIN");
        log.info(
            "try join member email[%s] pw[%s] %s"
                .formatted(initEmail, initPw, success?"SUCCESS" : "FAILED")
        );
    }

    public Optional<MemberEntity> findMemberByEmail(String email)
    {
        return memberRepository.findByEmail(email);
    }

    private boolean validCheckForEmail(String email)
    {
        if(email == null || email.equals(""))
            return false;
        String regexPattern = "^(.+)@(\\S+)$";
        boolean isRegexValid = Pattern.compile(regexPattern)
                .matcher(email)
                .matches()
        ;
        if(isRegexValid == false)
            return false;
        return true;
    }

    private boolean validCheckForPlanePw(String planePw)
    {
        if(planePw == null || planePw.equals(""))
            return false;
        return true;
    }

    public boolean signUp(String email, String planePw, String role)
    {
        //valid check for email
        if(validCheckForEmail(email) == false)
            return false;
    
        //valid check for plane password
        if(validCheckForPlanePw(planePw) == false)
            return false;

        var isExist = memberRepository.existsByEmail(email);
        if(isExist)
            return false;
        MemberEntity newMember = MemberEntity.builder()
            .id(null)
            .email(email)
            .password(planePw)
            .role(role)
            .isLocked(false)
            .build();
            
        newMember.doEncrypt(pwEncoder);
        memberRepository.save(newMember);
        return true;
    }

    public Iterable<MemberEntity> getAllMembers()
    {
        return memberRepository.findAll();
    }

    public void saveMember(MemberEntity member)
    {
        memberRepository.save(member);
    }
}