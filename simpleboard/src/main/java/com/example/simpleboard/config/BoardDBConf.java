package com.example.simpleboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class BoardDBConf {

    private String driverName;
    private String dbUrl;
    private String username;
    private String password;

    public BoardDBConf(
        @Value("${spring.datasource.driver-class-name}")
        String driverName
        , @Value("${spring.datasource.url}")
        String dbUrl
        , @Value("${spring.datasource.username}")
        String username
        , @Value("${spring.datasource.password}")
        String password
    )
    {
        this.driverName = driverName;
        this.dbUrl = dbUrl;
        this.username = username;
        this.password = password;
    }
}
