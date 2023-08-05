package org.yiqr.hibiscus.server.system.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author rose
 * @create 2023/7/20 00:06
 */
@Configuration
@ConditionalOnBean({DataSource.class})
@EnableJpaRepositories({"org.yiqr.**.repositories"})
@EntityScan({"org.yiqr.**.entity"})
public class JpaConfigure {

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
