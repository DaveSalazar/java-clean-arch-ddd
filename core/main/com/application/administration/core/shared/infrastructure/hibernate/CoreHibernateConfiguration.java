package com.application.administration.core.shared.infrastructure.hibernate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class CoreHibernateConfiguration {

    private final HibernateConfigurationFactory factory;

    public CoreHibernateConfiguration(HibernateConfigurationFactory factory) {
        this.factory = factory;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        return factory.hibernateTransactionManager(sessionFactory());
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory()  {
        return factory.sessionFactory(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        return factory.dataSource(
                "localhost",
                5454,
                "application",
                "application",
                "application"
        );
    }
}
