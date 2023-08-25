package com.example.simpleboard.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SimpleBoardUserDetailService implements UserDetailsService 
{
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        //find member.
        return User.builder().build();
    }
}
