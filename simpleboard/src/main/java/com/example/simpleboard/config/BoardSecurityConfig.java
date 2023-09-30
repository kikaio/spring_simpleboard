package com.example.simpleboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableMethodSecurity
public class BoardSecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception
    {
        final MvcRequestMatcher[] publicMatchers = {
            new MvcRequestMatcher(introspector, "/")
        };
        
        http
        .csrf(custom->{ custom.disable();})
        .cors(custom->{ custom.disable();})
        .authorizeHttpRequests(custom->{
            custom
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                .requestMatchers(publicMatchers).permitAll()
                .anyRequest().permitAll(); //member 작업 전까진 모두 permit All 처리.
        })
        ;
        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain exceptSecurityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception
    {
        //참고 블로그 : https://bitgadak.tistory.com/11
        //web ignore 역할 filter chain 추가 하면서 불필요한 filter는 disable 처리.
        final MvcRequestMatcher[] publicMatchers = {
            new MvcRequestMatcher(introspector, "/static/**")
            , new MvcRequestMatcher(introspector, "/favicon.ico")
            , new MvcRequestMatcher(introspector, "/h2-console/**")
        };

        http
        .csrf(custom->{ custom.disable();})
        .cors(custom->{ custom.disable();})
        .authorizeHttpRequests(custom->{
            custom
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                .requestMatchers(publicMatchers).permitAll()
                .anyRequest().permitAll()
            ;
        })
        .requestCache(custom->{
            custom.disable();
        })
        .securityContext(custom->{
            custom.disable();
        })
        .sessionManagement(custom->{
            custom.disable();
        })
        ;
        return http.build();
    }
}
