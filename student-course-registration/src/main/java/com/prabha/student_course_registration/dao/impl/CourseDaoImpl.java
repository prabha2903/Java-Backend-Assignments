package com.prabha.student_course_registration.dao.impl;

import com.prabha.student_course_registration.dao.CourseDao;
import com.prabha.student_course_registration.entity.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Pure Hibernate implementation of {@link CourseDao}.
 *
 * Each method opens its own {@link Session}, explicitly begins/commits (or
 * rolls back) a {@link Transaction}, and closes the Session via
 * try-with-resources. No Spring transaction management or Spring Data JPA
 * repository abstractions are used.
 */
@Repository
public class CourseDaoImpl implements CourseDao {

    private final SessionFactory sessionFactory;

    public CourseDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Course save(Course course) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(course);
            transaction.commit();
            return course;
        } catch (RuntimeException ex) {
            rollbackQuietly(transaction);
            throw ex;
        }
    }

    @Override
    public Optional<Course> findById(Long courseId) {
        try (Session session = sessionFactory.openSession()) {
            Course course = session.get(Course.class, courseId);
            return Optional.ofNullable(course);
        }
    }

    @Override
    public Optional<Course> findByIdWithStudents(Long courseId) {
        try (Session session = sessionFactory.openSession()) {
            // "left join fetch" eagerly initializes the lazy "students"
            // collection within this Session only, so it can be safely
            // converted to a DTO after the Session closes - the mapping
            // itself remains FetchType.LAZY.
            String hql = "select distinct c from Course c left join fetch c.students where c.courseId = :id";
            Course course = session.createQuery(hql, Course.class)
                    .setParameter("id", courseId)
                    .uniqueResult();
            return Optional.ofNullable(course);
        }
    }

    @Override
    public Optional<Course> findByCourseCode(String courseCode) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "from Course where courseCode = :courseCode";
            return session.createQuery(hql, Course.class)
                    .setParameter("courseCode", courseCode)
                    .uniqueResultOptional();
        }
    }

    private void rollbackQuietly(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}
