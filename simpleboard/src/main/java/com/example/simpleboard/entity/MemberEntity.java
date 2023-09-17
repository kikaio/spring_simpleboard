package com.example.simpleboard.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
    name="member"
    , indexes = {
        @Index(name="idx__email", columnList = "email", unique = true)
    }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    private String email;
    
    private String password;

    private String role;
    
    private boolean isLocked;

    private Collection<SimpleGrantedAuthority> authorities;

    public void doLock()
    {
        isLocked = true;
    }

    public void doUnlock()
    {
        isLocked = false;
    }

    public void setAuthorities(List<AuthorityEntity> entities)
    {
        authorities = new ArrayList<SimpleGrantedAuthority>();
        for(AuthorityEntity entity : entities)
        {
            authorities.add(new SimpleGrantedAuthority(entity.getAuthority()));
        }
    }
}
