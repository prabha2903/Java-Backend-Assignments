package com.prabha.employee_passport_mapping.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.prabha.employee_passport_mapping.entity.Employee;
import com.prabha.employee_passport_mapping.entity.Passport;

import jakarta.annotation.PreDestroy;

/**
 * Builds and exposes a single Hibernate SessionFactory as a Spring bean.
 *
 * This is PURE Hibernate configuration - no Spring Data JPA, no
 * LocalContainerEntityManagerFactoryBean. DAOs use this SessionFactory
 * directly to open Sessions and manage Transactions themselves.
 */
@Component
public class HibernateConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${hibernate.dialect}")
    private String dialect;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;

    @Value("${hibernate.show_sql:true}")
    private String showSql;

    @Value("${hibernate.format_sql:true}")
    private String formatSql;

    @Value("${hibernate.use_sql_comments:true}")
    private String useSqlComments;

    private SessionFactory sessionFactory;

    @Bean
    public SessionFactory sessionFactory() {
        Configuration configuration = new Configuration();

        configuration.setProperty("hibernate.connection.url", url);
        configuration.setProperty("hibernate.connection.username", username);
        configuration.setProperty("hibernate.connection.password", password);
        configuration.setProperty("hibernate.connection.driver_class", driverClassName);
        configuration.setProperty("hibernate.dialect", dialect);
        configuration.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        configuration.setProperty("hibernate.show_sql", showSql);
        configuration.setProperty("hibernate.format_sql", formatSql);
        configuration.setProperty("hibernate.use_sql_comments", useSqlComments);

        // Basic connection pool sizing (Hibernate's built-in pool - fine for dev/demo use)
        configuration.setProperty("hibernate.connection.pool_size", "10");

        configuration.addAnnotatedClass(Employee.class);
        configuration.addAnnotatedClass(Passport.class);

        this.sessionFactory = configuration.buildSessionFactory();
        return this.sessionFactory;
    }

    @PreDestroy
    public void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
