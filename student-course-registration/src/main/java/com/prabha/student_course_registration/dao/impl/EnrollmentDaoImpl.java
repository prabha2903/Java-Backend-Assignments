package com.prabha.student_course_registration.dao.impl;

import com.prabha.student_course_registration.dao.EnrollmentDao;
import com.prabha.student_course_registration.entity.Course;
import com.prabha.student_course_registration.entity.Student;
import com.prabha.student_course_registration.exception.CourseNotFoundException;
import com.prabha.student_course_registration.exception.DuplicateEnrollmentException;
import com.prabha.student_course_registration.exception.EnrollmentNotFoundException;
import com.prabha.student_course_registration.exception.StudentNotFoundException;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

/**
 * Pure Hibernate implementation of {@link EnrollmentDao}.
 *
 * Both the Student and Course involved in an enrollment change are loaded
 * and mutated within the SAME Session/Transaction so the join-table update
 * is atomic and the duplicate/absence check is consistent with the write.
 */
@Repository
public class EnrollmentDaoImpl implements EnrollmentDao {

    private final SessionFactory sessionFactory;

    public EnrollmentDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void enroll(Long studentId, Long courseId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            if (student == null) {
                throw new StudentNotFoundException("Student not found with id: " + studentId);
            }

            Course course = session.get(Course.class, courseId);
            if (course == null) {
                throw new CourseNotFoundException("Course not found with id: " + courseId);
            }

            // Initialize the lazy collection within this open Session so we
            // can safely check membership before mutating it.
            Hibernate.initialize(student.getCourses());

            if (student.getCourses().contains(course)) {
                throw new DuplicateEnrollmentException(
                        "Student with id " + studentId + " is already enrolled in course with id " + courseId);
            }

            student.addCourse(course);

            transaction.commit();
        } catch (RuntimeException ex) {
            rollbackQuietly(transaction);
            throw ex;
        }
    }

    @Override
    public void unenroll(Long studentId, Long courseId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            if (student == null) {
                throw new StudentNotFoundException("Student not found with id: " + studentId);
            }

            Course course = session.get(Course.class, courseId);
            if (course == null) {
                throw new CourseNotFoundException("Course not found with id: " + courseId);
            }

            Hibernate.initialize(student.getCourses());

            if (!student.getCourses().contains(course)) {
                throw new EnrollmentNotFoundException(
                        "Student with id " + studentId + " is not enrolled in course with id " + courseId);
            }

            student.removeCourse(course);

            transaction.commit();
        } catch (RuntimeException ex) {
            rollbackQuietly(transaction);
            throw ex;
        }
    }

    private void rollbackQuietly(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}
