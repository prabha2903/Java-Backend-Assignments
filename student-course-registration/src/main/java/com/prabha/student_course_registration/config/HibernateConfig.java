package com.prabha.student_course_registration.config;

import com.prabha.student_course_registration.entity.Course;
import com.prabha.student_course_registration.entity.Student;
import jakarta.annotation.PreDestroy;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Pure Hibernate configuration.
 *
 * This class builds a Hibernate {@link SessionFactory} programmatically using
 * {@link org.hibernate.cfg.Configuration} (the classic Hibernate API), instead
 * of relying on Spring Data JPA / JPA's {@code EntityManagerFactory}.
 *
 * The SessionFactory is exposed as a Spring bean purely as a convenience for
 * dependency injection into the DAO layer - Spring is only used here as a
 * wiring mechanism, not as an ORM abstraction.
 */
@Configuration
public class HibernateConfig {

    @Value("${spring.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

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
        // org.hibernate.cfg.Configuration is fully-qualified here to avoid a
        // naming clash with Spring's own @Configuration annotation.
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration.setProperty("hibernate.connection.driver_class", driverClassName);
        configuration.setProperty("hibernate.connection.url", url);
        configuration.setProperty("hibernate.connection.username", username);
        configuration.setProperty("hibernate.connection.password", password);
        configuration.setProperty("hibernate.dialect", dialect);
        configuration.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        configuration.setProperty("hibernate.show_sql", showSql);
        configuration.setProperty("hibernate.format_sql", formatSql);
        configuration.setProperty("hibernate.connection.pool_size", poolSize);

        // Register the annotated entity classes. This is where the
        // @ManyToMany / @JoinTable mapped student_course join table gets
        // discovered and, thanks to hbm2ddl.auto=update, created automatically.
        configuration.addAnnotatedClass(Student.class);
        configuration.addAnnotatedClass(Course.class);

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
