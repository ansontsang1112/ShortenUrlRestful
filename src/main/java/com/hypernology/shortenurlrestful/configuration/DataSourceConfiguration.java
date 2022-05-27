package com.hypernology.shortenurlrestful.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    @Autowired
    private Environment environment;

    @Bean
    public DataSource mysqlDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url(environment.getProperty("mysql.url"));
        dataSourceBuilder.username(environment.getProperty("mysql.username"));
        dataSourceBuilder.password(environment.getProperty("mysql.password"));
        return dataSourceBuilder.build();
    }
}
