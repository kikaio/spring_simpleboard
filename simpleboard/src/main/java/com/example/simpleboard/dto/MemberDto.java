package com.example.simpleboard.dto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private boolean enabled = true;
    private boolean expired = false;
    private boolean locked = false; // todo : 추후 이메일 인증 로직 통과 후 false로 바꿀 예정.

    public MemberDto(Member entity)
    {
        fromEntity(entity);
    }
    
    public void doPasswordEncrypt()
    {
        this.doPasswordEncrypt(new BCryptPasswordEncoder());
    }

    public void doPasswordEncrypt(PasswordEncoder encoder)
    {
        password = encoder.encode(password);
    }

    public void fromEntity(Member entity)
    {
        if(entity == null)
            return ;
            
        id = entity.getId();
        email = entity.getEmail();
        password = entity.getPassword();
        enabled = entity.isEnabled();
        expired = entity.isExpired();
        locked = entity.isLocked();
    }

    public Member toEntity()
    {
        return Member.builder()
            .id(this.id)
            .email(this.email)
            .password(this.password)
            .enabled(enabled)
            .expired(expired)
            .locked(locked)
            .build()
        ;
    }
}
