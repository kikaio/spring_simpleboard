package com.example.simpleboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SimpleBoardSecureConfig {

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.cors(cors->cors.disable())
            .csrf(csrf->csrf.disable())
            .authorizeHttpRequests(req->
            {
                req.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                    .requestMatchers(
                        "/"
                        , "/member/sign-up", "/member/sign-up-process", "/member/sign-in"
                        , "/member/list"
                        , "/static", "/status", "/images/**"
                    ).permitAll()
                    .anyRequest().authenticated()
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
            .logout(withDefaults())
            ;
        ;
        return http.build();
    }
}
