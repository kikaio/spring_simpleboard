package com.example.simpleboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SimpleBoardSecureConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.cors(cors->cors.disable())
            .csrf(csrf->csrf.disable())
            .authorizeHttpRequests(req->{
                req.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                    .requestMatchers("/", "/static", "/status", "/images/**").permitAll()
                    .anyRequest().authenticated()
                    ;
            })
            .formLogin(req->{
                req.loginPage("/view/login")
                    .loginProcessingUrl("/login-process")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/view/dashboard", true)
                    .permitAll()
                ;
            })
            .logout(withDefaults())
            ;
        ;
        return http.build();
    }
}
