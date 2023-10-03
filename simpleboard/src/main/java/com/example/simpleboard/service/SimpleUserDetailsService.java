package com.example.simpleboard.service;

import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.simpleboard.entity.Member;
import com.example.simpleboard.repository.MemberRepository;

@Service
public class SimpleUserDetailsService implements UserDetailsService
{

    private final MemberRepository memberRepository;

    public SimpleUserDetailsService(MemberRepository memberRepository)
    {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        var member = memberRepository.findByEmail(username)
            .orElseThrow(()->
            {
                return new UsernameNotFoundException("email[%s] not found.".formatted(username));
            }
        );

        var allAuthorities = Stream.concat(
            getAuthorities(member).stream(), getRolesGrant(member).stream())
            .collect(Collectors.toSet()
        );
        
        member.setAuthoirties(allAuthorities);
        return member;
    }

    //role 이 보유한 모든 privilege를 정리.
    private Collection<SimpleGrantedAuthority> getAuthorities(Member member)
    {
        var authorities = member.getRoles().stream()
            .flatMap(role->role.getPrivileges().stream())
            .map(ele->new SimpleGrantedAuthority(ele.getName()))
            .collect(Collectors.toList())
        ;
        return authorities;
    }

    private Collection<SimpleGrantedAuthority> getRolesGrant(Member member)
    {
        var roleGrants = member.getRoles().stream()
            .map(ele->new SimpleGrantedAuthority("ROLE_%s".formatted(ele.getName())))
            .collect(Collectors.toSet())
        ;
        return roleGrants;
    }
}
