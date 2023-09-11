package com.example.simpleboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Optional;

import org.apache.catalina.authenticator.SavedRequest;


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
            , signUpUrl, signUpProcessUrl, signInUrl
            , "/member/list"
            , "/static", "/status", "/images/**"
        };
        http.cors(cors->cors.disable())
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
                    .defaultSuccessUrl(signInSuccessUrl, true)
                    .permitAll()
                    .successHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
                            //login success url 말고 직전에 있던 url 로 redirect 처리
                            HttpSessionRequestCache reqCache = new HttpSessionRequestCache();
                            var savedReq = reqCache.getRequest(request, response);
                            String redirecUrl = savedReq.getRedirectUrl();
                            UserDetails user = (UserDetails)authentication.getPrincipal();
                            System.out.println("redirect url : %s".formatted(redirecUrl));
                            response.sendRedirect(redirecUrl);
                    })
                    .failureHandler((request, response, exception) -> {
                        String username =request.getAttribute("email").toString();
                        String password = request.getAttribute("password").toString();
                        System.out.println("user[%s] failed sign in. try password [%s]".formatted(username, password));
                    })
                ;
            })
            .logout(req->{
                req.logoutUrl(signOutUrl)
                    .addLogoutHandler((request, response, authentication) -> {
                        var session = request.getSession();
                        if(session != null)
                            session.invalidate();
                    })
                    .logoutSuccessHandler((request, response, authentication) -> {
                        response.sendRedirect(signInUrl);
                    })
                ;
            })
            ;
        ;
        return http.build();
    }
}
