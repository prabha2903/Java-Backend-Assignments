package com.prabha.student_course_registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Application entry point.
 *
 * Spring Boot's {@link DataSourceAutoConfiguration} is excluded because this
 * project deliberately does NOT use Spring Data JPA / a Spring-managed
 * DataSource + JPA EntityManager. Persistence is handled entirely through a
 * hand-configured Hibernate {@link org.hibernate.SessionFactory} (see
 * {@code config.HibernateConfig}), which opens its own JDBC connections
 * directly against MySQL.
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class StudentCourseRegistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentCourseRegistrationApplication.class, args);
	}

}
