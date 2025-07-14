package com.mapmaker.config;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://db:5432/map_app");
        dataSource.setUsername("postgres");
        dataSource.setPassword("9632");
        return dataSource;
    }
}