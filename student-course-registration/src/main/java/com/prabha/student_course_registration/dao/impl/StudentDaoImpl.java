package com.prabha.student_course_registration.dao.impl;

import com.prabha.student_course_registration.dao.StudentDao;
import com.prabha.student_course_registration.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Pure Hibernate implementation of {@link StudentDao}.
 *
 * Each method opens its own {@link Session}, explicitly begins/commits (or
 * rolls back) a {@link Transaction}, and closes the Session via
 * try-with-resources. No Spring transaction management or Spring Data JPA
 * repository abstractions are used.
 */
@Repository
public class StudentDaoImpl implements StudentDao {

    private final SessionFactory sessionFactory;

    public StudentDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Student save(Student student) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
            return student;
        } catch (RuntimeException ex) {
            rollbackQuietly(transaction);
            throw ex;
        }
    }

    @Override
    public Optional<Student> findById(Long studentId) {
        try (Session session = sessionFactory.openSession()) {
            Student student = session.get(Student.class, studentId);
            return Optional.ofNullable(student);
        }
    }

    @Override
    public Optional<Student> findByIdWithCourses(Long studentId) {
        try (Session session = sessionFactory.openSession()) {
            // "left join fetch" eagerly initializes the lazy "courses"
            // collection within this Session only, so it can be safely
            // converted to a DTO after the Session closes - the mapping
            // itself remains FetchType.LAZY.
            String hql = "select distinct s from Student s left join fetch s.courses where s.studentId = :id";
            Student student = session.createQuery(hql, Student.class)
                    .setParameter("id", studentId)
                    .uniqueResult();
            return Optional.ofNullable(student);
        }
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "from Student where email = :email";
            return session.createQuery(hql, Student.class)
                    .setParameter("email", email)
                    .uniqueResultOptional();
        }
    }

    private void rollbackQuietly(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}
