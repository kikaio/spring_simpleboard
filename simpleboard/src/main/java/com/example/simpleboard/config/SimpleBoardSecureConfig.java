package com.example.simpleboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Optional;

@Configuration
@EnableMethodSecurity
@Slf4j
public class SimpleBoardSecureConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    private final int maxSessionCnt = 3;

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    // @PostConstruct
    // public void init()
    // {
    //     String initEmail = "admin@admin.com";
    //     String initPw = "1234";
    //     if(userDetailsService instanceof SimpleBoardUserDetailService)
    //     {
    //         var curService = (SimpleBoardUserDetailService)this.userDetailsService;
            
    //         boolean success = curService.signUp(initEmail, passwordEncoder().encode(initPw), "ADMIN");
    //         log.info(
    //             "try join member email[%s] pw[%s] %s"
    //                 .formatted(initEmail, initPw, success?"SUCCESS" : "FAILED")
    //         );
    //     }
    // }

    @Bean
    public WebSecurityCustomizer webSecurityCustumizer()
    {
        final String faviconPath = "/favicon.ico";
        final String errorPath = "/error";
        String[] patterns = {
            // faviconPath
            // , errorPath
        };
        return (web)->{
            web.ignoring().requestMatchers(
                patterns
            );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        final String indexUrl = "/";

        final String signInUrl = "/member/sign-in";
        final String signInProcessUrl = "/member/sign-in-process";
        final String signInSuccessUrl = "/member/sign-in-success";
        final String signUpUrl = "/member/sign-up";
        final String signUpProcessUrl = "/member/sign-up-process";
        final String signOutUrl = "/member/sign-out"; 

        String[] permitAlls = {
            indexUrl
            , signInUrl, signInProcessUrl 
            , signUpUrl, signUpProcessUrl
            , "/member/list"
            , "/static", "/static/favicon.ico"
            , "/error", "/status", "/images/**"
        };
        http.cors(cors->cors.disable())
            .csrf(conf->conf.disable())
            .authorizeHttpRequests(req->
            {
                req.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                    .requestMatchers(
                        permitAlls
                    ).permitAll()
                    .anyRequest().authenticated()
                    ;
            })
            .sessionManagement(sessionConf->{
                //session max 갯수 지정 및 초과 로그인 시 세션 처리 설정
                sessionConf.maximumSessions(maxSessionCnt)
                    .maxSessionsPreventsLogin(true)
                    ;

                sessionConf.sessionFixation(fixationConfig->{
                    //session 관리 모드 지정, user가 login 시 session id 발급에 대하여.
                    fixationConfig.none();
                    // fixationConfig.changeSessionId();
                    // fixationConfig.migrateSession();
                    // fixationConfig.newSession();
                });
            })
            .exceptionHandling(exceptConf->{
                exceptConf.authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect(signInUrl);
                });
            })
            .rememberMe(req->{
                req.rememberMeCookieName("remember_cookie")
                    .rememberMeParameter("remember")
                    .tokenValiditySeconds(60 * 60 * 24 * 7 * 3) //3weeks
                    .userDetailsService(userDetailsService)
                    ;
            })
            .formLogin(conf->
            {
                conf.loginPage(signInUrl)
                    .loginProcessingUrl(signInProcessUrl)
                    .usernameParameter("email")
                    .passwordParameter("password")
                    //.defaultSuccessUrl(signInSuccessUrl, true)
                    .permitAll();
                
                conf.successHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> 
                    {
                            //login success url 말고 직전에 있던 url 로 redirect 처리
                            var session = request.getSession(false);
                            if(session != null)
                            {
                                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                            }
                            final var requestCache = new HttpSessionRequestCache(); 
                            SavedRequest savedRequest = requestCache.getRequest(request, response);
                            
                            String redirectUrl = indexUrl;
                            String prevPage = (String)session.getAttribute("prevPage");
                            if(prevPage != null)
                            {
                                session.removeAttribute("prevPage");
                            }
                            if(savedRequest != null)
                            {
                                redirectUrl = savedRequest.getRedirectUrl();
                            }
                            else if(prevPage != null && !prevPage.equals(""))
                            {
                                if(prevPage.equals(signInUrl))
                                {
                                    redirectUrl = indexUrl;
                                }
                                else
                                {
                                    redirectUrl = prevPage;
                                }
                            }
                            log.info("login success, redirect to [%s]".formatted(redirectUrl));

                            RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
                            redirectStrategy.sendRedirect(request, response, redirectUrl);
                    })
                    .failureHandler((request, response, exception) -> 
                    {
                        String username =request.getParameter("email").toString();
                        String password = request.getParameter("password").toString();
                        System.out.println("user[%s] failed sign in. try password [%s]".formatted(username, password));
                    });
            })
            .logout(req->{
                req.logoutUrl(signOutUrl)
                    .addLogoutHandler((request, response, authentication) -> {
                        var session = request.getSession();
                        if(session != null)
                            session.invalidate();
                    })
                    .logoutSuccessHandler((request, response, authentication) -> {
                        response.sendRedirect(indexUrl);
                    })
                ;
            })
            ;
        ;
        return http.build();
    }
}
