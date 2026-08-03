package com.prabha.hospital_management.config;

import com.prabha.hospital_management.entity.Appointment;
import com.prabha.hospital_management.entity.Doctor;
import com.prabha.hospital_management.entity.Patient;
import jakarta.annotation.PreDestroy;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

/**
 * Pure Hibernate configuration. Builds a Hibernate {@link SessionFactory}
 * directly from {@link Configuration}, {@link StandardServiceRegistryBuilder}
 * without relying on Spring Data JPA or Spring ORM's LocalSessionFactoryBean.
 */
@org.springframework.context.annotation.Configuration
public class HibernateConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}")
    private String driverClassName;

    @Value("${hibernate.dialect:org.hibernate.dialect.MySQLDialect}")
    private String dialect;

    @Value("${hibernate.hbm2ddl.auto:update}")
    private String hbm2ddlAuto;

    @Value("${hibernate.show_sql:true}")
    private String showSql;

    @Value("${hibernate.format_sql:true}")
    private String formatSql;

    @Value("${hibernate.connection.pool_size:10}")
    private String poolSize;

    private SessionFactory sessionFactory;

    @Bean
    public SessionFactory sessionFactory() {
        Properties settings = new Properties();
        settings.put(Environment.DRIVER, driverClassName);
        settings.put(Environment.URL, url);
        settings.put(Environment.USER, username);
        settings.put(Environment.PASS, password);
        settings.put(Environment.DIALECT, dialect);
        settings.put(Environment.HBM2DDL_AUTO, hbm2ddlAuto);
        settings.put(Environment.SHOW_SQL, showSql);
        settings.put(Environment.FORMAT_SQL, formatSql);
        settings.put(Environment.POOL_SIZE, poolSize);

        Configuration configuration = new Configuration();
        configuration.setProperties(settings);
        configuration.addAnnotatedClass(Doctor.class);
        configuration.addAnnotatedClass(Patient.class);
        configuration.addAnnotatedClass(Appointment.class);

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        this.sessionFactory = configuration.buildSessionFactory(registry);
        return this.sessionFactory;
    }

    @PreDestroy
    public void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
