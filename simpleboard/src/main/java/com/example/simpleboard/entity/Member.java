package com.example.simpleboard.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Member implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private boolean expired;

    @Column
    private boolean locked;

    @Column
    private boolean enabled;

    @ManyToMany(
        fetch = FetchType.EAGER
        , cascade = {
            CascadeType.PERSIST, CascadeType.MERGE
        }
    )
    @JoinTable(
        name="member_role"
        , joinColumns = {
            @JoinColumn(referencedColumnName = "id", name="member_id")
        }
        , inverseJoinColumns = {
            @JoinColumn(referencedColumnName = "id", name="role_id")
        }
    )
    private final Set<Role> roles = new HashSet<>();

    @Transient
    private Collection<SimpleGrantedAuthority> authorities;

    public void setAuthoirties(Collection<SimpleGrantedAuthority> authorities)
    {
        this.authorities = authorities;
    }

    @Override
    public String getUsername()
    {
        return email;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return false;
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    @Override
    public boolean equals(Object other)
    {
        if(other == this)
        {
            return true;
        }
        if(other == null)
        {
            return false;
        }
        if(other.getClass() != getClass())
        {
            return false;
        }
        //단순하게 email, password 일치 여부만 판별.
        var mem = (Member)other;
        if(mem.getEmail() != getEmail() || mem.getPassword() != getPassword())
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        return getEmail().hashCode();
    }


    public void updateOnlyMember(Member other)
    {
        if(other.password != null)
        {
            this.password = other.password;
        }
        this.enabled = other.enabled;
        this.expired = other.expired;
        this.locked = other.locked;
    }

}
