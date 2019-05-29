package com.impl.homesecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;

/**
 * Created by vyacheslav on 19.10.18.
 */
@Configuration
@EnableTransactionManagement
public class MultipleDBConfig {

    @Bean(name = "mysqlMain")
    @ConfigurationProperties(prefix = "spring.datasource1")
    public DataSource mysqlDataSourceMain() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mysqlJdbcTemplateMain")
    public JdbcTemplate jdbcTemplateMain(@Qualifier("mysqlMain") DataSource dsMySQL) {
        return new JdbcTemplate(dsMySQL);
    }

    @Bean(name = "mysqlApp")
    @ConfigurationProperties(prefix = "spring.datasource2")
    public DataSource mysqlDataSourceApp() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "mysqlJdbcTemplateApp")
    public JdbcTemplate jdbcTemplateApp(@Qualifier("mysqlApp") DataSource dsMySQL) {
        return new JdbcTemplate(dsMySQL);
    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(@Qualifier("mysqlApp") DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
