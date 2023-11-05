package com.example.simpleboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class BoardJpaConf {
    private boolean showSql;
    private boolean formatSql;
    private boolean useSqlComments;
    private String database;
    private String databasePlatform;
    private boolean generateDDL;
    private String ddlAuto;

    public BoardJpaConf(
        @Value("${spring.jpa.properties.hibernate.show_sql}")
        boolean showSql
        , @Value("${spring.jpa.properties.hibernate.format_sql}")
        boolean formatSql
        , @Value("${spring.jpa.properties.hibernate.use_sql_comments}")
        boolean useSqlComments
        , @Value("${spring.jpa.database}")
        String database
        , @Value("${spring.jpa.database-platform}")
        String databasePlatform
        , @Value("${spring.jpa.generate-ddl}")
        boolean generateDDL
        , @Value("${spring.jpa.hibernate.ddl-auto}")
        String ddlAuto
    )
    {
        this.showSql = showSql;
        this.formatSql = formatSql;
        this.useSqlComments = useSqlComments;
        this.database = database;
        this.databasePlatform = databasePlatform;
        this.generateDDL = generateDDL;
        this.ddlAuto = ddlAuto;
    }
}
