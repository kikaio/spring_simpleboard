package com.example.simpleboard.config.auth.dto;

import java.util.Map;

import com.example.simpleboard.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OAuthAttribute {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    public static OAuthAttribute of(String registrationId, String userNameAttrName, Map<String, Object> attrs)
    {
        return fromGoogle(userNameAttrName, attrs);
    }

    private static OAuthAttribute fromGoogle(String usernameAttrName, Map<String, Object> attr)
    {
        return OAuthAttribute.builder()
            .name((String)attr.get("name"))
            .email((String)attr.get("email"))
            .picture((String)attr.get("picture"))
            .attributes(attr)
            .nameAttributeKey(usernameAttrName)
            .build()
        ;
    }

    public Member toEntitywithoutRole()
    {
        var member =  Member.builder()
            .email(email)
            .enabled(true)
            .expired(false)
            .locked(false)
            .build()
        ;
        return member;
    }
}
