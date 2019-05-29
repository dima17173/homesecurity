package com.impl.homesecurity.config;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_7_10;

/**
 * Конфигурация базы Embedded Mysql Wix
 */
@Configuration
public class EmbeddedMySQLConfig {

    EmbeddedMysql mysqld;

    @Value("${spring.mysql.port}")
    private int mysqlPort;

    @PostConstruct
    public EmbeddedMysql runEmbeddedMySQLTest()  {

        MysqldConfig config = aMysqldConfig(v5_7_10)
                .withPort(mysqlPort)
                .withUser("admin", "admin")
                .build();

        return mysqld = anEmbeddedMysql(config)
                .addSchema("c2testforagent")
                .start();
    }

    @PreDestroy
    public void stopEmbeddedMySQL(){
        mysqld.stop();
    }

}
