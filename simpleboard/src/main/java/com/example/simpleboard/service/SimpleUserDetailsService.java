package com.example.simpleboard.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.simpleboard.entity.Member;
import com.example.simpleboard.entity.Role;
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

    public boolean createMember(String email, String  password)
    {
        Member member = Member.builder()
            .email(email)
            .password(password)
            .expired(false)
            .locked(false)
            .enabled(true)
            .build()
        ;

        try {
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean createMember(Member member)
    {
        try {
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Page<Member> getMembers(Pageable pageable)
    {
        try {
            return memberRepository.findAll(pageable);
        } catch (Exception e) {
            return new PageImpl<Member>(new ArrayList<Member>());
        }
    }

    public Member getMember(Long id)
    {
        try {
            return memberRepository.findById(id).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public Member getMember(String email)
    {
        try {
            return memberRepository.findByEmail(email).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updateOnlyMember(Member newMember)
    {
        try {
            var origin = memberRepository.findById(newMember.getId()).orElseThrow(()->{
                return new UsernameNotFoundException(
                    "member[id:%d] not found in repo"
                    .formatted(newMember.getId())
                );
            });
            origin.updateOnlyMember(newMember);
            memberRepository.save(origin);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateOnlyRoles(Member target, HashSet<Role> newRoles)
    {
        try {
            var member = memberRepository.findById(target.getId()).orElseThrow(()->{
                return new UsernameNotFoundException("member id[%d] not exist ni repo".formatted());
            });
            member.updateOnlyRole(newRoles);
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteMember(Long id)
    {
        try {
            memberRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }    

    public boolean validCheckCreateMember(Member member)
    {
        if(member == null)
        {
            return false;
        }
        if(member.getPassword() == null || member.getPassword() == "")
        {
            return false;
        }
        //todo : email 형식 확인할 것.
        if(member.getEmail() == null)
        {
            return false;
        }

        return true;
    }
}
