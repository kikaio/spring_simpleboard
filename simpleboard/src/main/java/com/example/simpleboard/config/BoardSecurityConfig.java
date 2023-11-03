package com.example.simpleboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.example.simpleboard.entity.Member;
import com.example.simpleboard.service.SimpleUserDetailsService;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableMethodSecurity
public class BoardSecurityConfig {

    @Autowired
    private SimpleUserDetailsService simpleUserDetailsService;

    @Bean
    public PasswordEncoder getPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception
    {
        final String signInUrl = "/sign/sign-in";
        final String signInProcessUrl = "/sign/sign-in-process";
        final String signInFailureUrl = "/sign/sign-in-failed";
        final String signInSuccessUrl = "/sign/sign-in-success";
        final String signUpUrl = "/sign/sign-up";
        final String signUpProcessUrl = "/sign/sign-up-process";
        final String signUpFailedUrl = "/sign/sign-up-failed";
        final String signUpSuccessUrl = "/sign/sign-up-success";
        final String signOutUrl = "/sign/sign-out";
        final String signOutSuccessUrl = signInUrl;

        final String loginIdParam = "email";
        final String loginPasswordParam = "password";


        final String signTestUrl = "/sign/test";

        final MvcRequestMatcher[] signMatchers = {
            new MvcRequestMatcher(introspector, signInUrl)
            , new MvcRequestMatcher(introspector, signInProcessUrl)
            , new MvcRequestMatcher(introspector, signInFailureUrl)
            , new MvcRequestMatcher(introspector, signInSuccessUrl)
            , new MvcRequestMatcher(introspector, signUpUrl)
            , new MvcRequestMatcher(introspector, signUpProcessUrl)
            , new MvcRequestMatcher(introspector, signUpFailedUrl)
            , new MvcRequestMatcher(introspector, signUpSuccessUrl)
            , new MvcRequestMatcher(introspector, signOutUrl)
            , new MvcRequestMatcher(introspector, signOutSuccessUrl)
            , new MvcRequestMatcher(introspector, signTestUrl)
        };

        final MvcRequestMatcher[] publicMatchers = {
            new MvcRequestMatcher(introspector, "/")
        };
        
        http
        .csrf(custom->{ custom.disable();})
        .cors(custom->{ custom.disable();})
        .formLogin(custom->{
            custom.loginPage(signInUrl)
               .loginProcessingUrl(signInProcessUrl)
                .usernameParameter(loginIdParam)
                .passwordParameter(loginPasswordParam)
                .failureUrl(signInFailureUrl)
                .successHandler((request, response, authentication) -> {
                    var user = (Member)authentication.getPrincipal();
                    var session = request.getSession();
                    session.setAttribute("username", user.getUsername());
                    session.setAttribute("user", user);
                    response.sendRedirect(signInSuccessUrl);
                })
            ;
        })
        .userDetailsService(simpleUserDetailsService)
        .logout(custom->{
            custom.logoutUrl(signOutUrl)
                .logoutSuccessUrl(signOutSuccessUrl)
            ;
        })
        .authorizeHttpRequests(custom->{
            custom
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                .requestMatchers(signMatchers).permitAll()
                .requestMatchers(publicMatchers).permitAll()
                .anyRequest().authenticated(); //member 작업 전까진 모두 permit All 처리.
        })
        ;
        return http.build();
    }

    @Bean 
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    } 

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(HandlerMappingIntrospector introspector)
    {
         final String staticCssResourcesPath = "/css/**";
        final String staticImagesResourcesPath = "/images/**";
        final String staticScriptsResourcesPath = "/js/**";
        final String faviconResourcePath = "/favicon.ico";
        final String h2SConsoleUrl = "/h2-console/**";
        //참고 블로그 : https://bitgadak.tistory.com/11
        //web ignore 역할 filter chain 추가 하면서 불필요한 filter는 disable 처리.
        final MvcRequestMatcher[] publicMatchers = {
            new MvcRequestMatcher(introspector, staticCssResourcesPath)
            , new MvcRequestMatcher(introspector, staticImagesResourcesPath)
            , new MvcRequestMatcher(introspector, staticScriptsResourcesPath)
            , new MvcRequestMatcher(introspector, faviconResourcePath)
            , new MvcRequestMatcher(introspector, h2SConsoleUrl)
        };
        return (web)->{
            web.ignoring().requestMatchers(publicMatchers);
        };
    }

    // @Bean
    // @Order(1)
    // public SecurityFilterChain exceptSecurityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception
    // {
    //     final String staticCssResourcesPath = "/css/**";
    //     final String staticImagesResourcesPath = "/images/**";
    //     final String staticScriptsResourcesPath = "/js/**";
    //     final String faviconResourcePath = "/favicon.ico";
    //     final String h2SConsoleUrl = "/h2-console/**";
    //     //참고 블로그 : https://bitgadak.tistory.com/11
    //     //web ignore 역할 filter chain 추가 하면서 불필요한 filter는 disable 처리.
    //     final MvcRequestMatcher[] publicMatchers = {
    //         new MvcRequestMatcher(introspector, staticCssResourcesPath)
    //         , new MvcRequestMatcher(introspector, staticImagesResourcesPath)
    //         , new MvcRequestMatcher(introspector, staticScriptsResourcesPath)
    //         , new MvcRequestMatcher(introspector, faviconResourcePath)
    //         , new MvcRequestMatcher(introspector, h2SConsoleUrl)
    //     };

    //     http
    //     .csrf(custom->{ custom.disable();})
    //     .cors(custom->{ custom.disable();})
    //     .authorizeHttpRequests(custom->{
    //         custom
    //             .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
    //             .requestMatchers(publicMatchers).permitAll()
    //             .anyRequest().permitAll()
    //         ;
    //     })
    //     .requestCache(custom->{
    //         custom.disable();
    //     })
    //     .securityContext(custom->{
    //         custom.disable();
    //     })
    //     .sessionManagement(custom->{
    //         custom.disable();
    //     })
    //     ;
    //     return http.build();
    // }
}
