package com.impl.homesecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({LiquibaseProperties.class})
@EnableAspectJAutoProxy
@EnableConfigServer
public class HomeSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeSecurityApplication.class, args);
    }
}
