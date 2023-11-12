package com.example.simpleboard.config.auth.dto;

import java.io.Serializable;

import com.example.simpleboard.entity.Member;

import lombok.Getter;

@Getter
public class SessionUser implements Serializable{
    private long id;
    private String name;
    private String email;

    public SessionUser(Member member)
    {
        this.id = member.getId();
        this.name = member.getUsername();
        this.email = member.getEmail();
    }
}
