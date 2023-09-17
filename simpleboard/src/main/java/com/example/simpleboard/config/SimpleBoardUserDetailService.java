package com.example.simpleboard.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.simpleboard.entity.MemberEntity;
import com.example.simpleboard.repositoty.AuthorityRepository;
import com.example.simpleboard.service.MemberService;

@Service
public class SimpleBoardUserDetailService implements UserDetailsService 
{
    private final MemberService memberService;
    private final AuthorityRepository authorityRepository;

    public SimpleBoardUserDetailService(MemberService memberService, AuthorityRepository authorityRepository)
    {
        this.memberService = memberService;
        this.authorityRepository = authorityRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        //find member.
        var target = memberService.findMemberByEmail(email);
        MemberEntity member = target.orElseThrow(()->
        { 
            throw new UsernameNotFoundException("email[%s] not exist in repo".formatted(email));
        });

        var authorities = authorityRepository.findAllByMemberId(member.getId());
        member.setAuthorities(authorities);
        
        return User.builder()
            .username(member.getEmail())
            .password(member.getPassword())
            .roles(member.getRole())
            .accountLocked(member.isLocked())
            .authorities(member.getAuthorities())
            .build();
    }

    public boolean signUp(String id, String pw, String role)
    {
        return this.memberService.signUp(id, pw, role);
    }
}
