package com.example.simpleboard.dto;

import org.springframework.stereotype.Component;

import com.example.simpleboard.entity.Member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberDto {

    private Long id;

    private String email;

    private String password;

    public MemberDto(Member entity)
    {
        fromEntity(entity);
    }
    
    public void fromEntity(Member entity)
    {
        if(entity == null)
            return ;
            
        id = entity.getId();
        email = entity.getEmail();
        password = entity.getPassword();
    }

    public Member toEntity()
    {
        return Member.builder()
            .id(this.id)
            .email(this.email)
            .password(this.password)
            .build()
        ;
    }
}
