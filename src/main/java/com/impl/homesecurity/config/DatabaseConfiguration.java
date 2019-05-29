package com.impl.homesecurity.config;

import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Created by dima.
 * Creation date 19.10.18.
 */
@Configuration
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Bean
    public SpringLiquibase liquibase(@Qualifier("mysqlApp")DataSource dataSource, LiquibaseProperties liquibaseProperties) {

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setDropFirst(false);
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        if (log.isDebugEnabled()) {
            log.debug("Configuring Liquibase");
        }
        return liquibase;
    }

    @Bean
    @Profile("devsec")
    public SpringLiquibase liquibaseLora(@Qualifier("mysqlMain")DataSource dataSource, LiquibaseProperties liquibaseProperties) {

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:config/liquibase/masterlora.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setDropFirst(false);
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        if (log.isDebugEnabled()) {
            log.debug("Configuring Liquibase");
        }
        return liquibase;
    }
}
