package com.example.simpleboard.entity;

import java.math.BigInteger;

import org.springframework.context.annotation.Bean;

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

    public void doLock()
    {
        isLocked = true;
    }

    public void doUnlock()
    {
        isLocked = false;
    }
}
