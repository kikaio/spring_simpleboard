package com.example.simpleboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableMethodSecurity
public class SimpleBoardSecureConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        String[] permitAlls = {
            "/"
            , "/member/sign-up", "/member/sign-up-process", "/member/sign-in"
            , "/member/list"
            , "/static", "/status", "/images/**"
        };
        http.cors(cors->cors.disable())
            .csrf(csrf->csrf.disable())
            .authorizeHttpRequests(req->
            {
                req.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                    .requestMatchers(
                        permitAlls
                    ).permitAll()
                    .anyRequest().authenticated()
                    ;
            })
            .rememberMe(req->{
                req.rememberMeCookieName("remember_cookie")
                    .rememberMeParameter("remember")
                    .tokenValiditySeconds(60 * 60 * 24 * 7 * 3) //3weeks
                    .userDetailsService(userDetailsService)
                    ;
            })
            .formLogin(req->
            {
                req.loginPage("/member/sign-in")
                    .loginProcessingUrl("/member/sign-in-process")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/member/sign-in-success", true)
                    .permitAll()
                ;
            })
            .logout(req->{
                req.logoutUrl("/member/sign-out")
                    .addLogoutHandler((request, response, authentication) -> {
                        var session = request.getSession();
                        if(session != null)
                            session.invalidate();
                    })
                    .logoutSuccessHandler((request, response, authentication) -> {
                        response.sendRedirect("/member/sign-in");
                    })
                ;
            })
            ;
        ;
        return http.build();
    }
}
