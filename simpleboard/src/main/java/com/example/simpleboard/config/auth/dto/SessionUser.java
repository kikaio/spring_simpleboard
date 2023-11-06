package com.example.simpleboard.config.auth.dto;

import java.io.Serializable;

import com.example.simpleboard.entity.Member;

import lombok.Getter;

@Getter
public class SessionUser implements Serializable{
    private String name;
    private String email;

    public SessionUser(Member member)
    {
        this.name = member.getUsername();
        this.email = member.getEmail();
    }
}
