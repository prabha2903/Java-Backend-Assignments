package com.prabha.student_course_registration;

import com.prabha.student_course_registration.entity.Course;
import com.prabha.student_course_registration.entity.Student;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies that the Student &lt;-&gt; Course Many-to-Many association is
 * genuinely LAZY:
 * <ul>
 *   <li>The "courses" collection is NOT loaded when a Student is simply fetched.</li>
 *   <li>Accessing the collection AFTER the owning Session is closed throws a
 *       {@link LazyInitializationException} (proving it is not eagerly loaded).</li>
 *   <li>Accessing/initializing the collection WHILE the Session is still open
 *       works correctly, with no exception.</li>
 *   <li>An HQL "join fetch" query can eagerly initialize the collection
 *       on demand, without changing the FetchType.LAZY mapping itself.</li>
 * </ul>
 *
 * Requires a running MySQL instance reachable via the datasource properties
 * in application.properties, since it exercises the real Hibernate SessionFactory.
 */
@SpringBootTest
class LazyLoadingVerificationTest {

    @Autowired
    private SessionFactory sessionFactory;

    private Long studentId;
    private Long courseId;

    @BeforeEach
    void setUp() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Student student = new Student(
                    "Lazy Test Student",
                    "lazy.test." + System.nanoTime() + "@example.com",
                    "CSE");
            Course course = new Course("Lazy Loading Fundamentals", "LL" + System.nanoTime(), 3);

            session.persist(student);
            session.persist(course);

            student.addCourse(course);

            tx.commit();

            studentId = student.getStudentId();
            courseId = course.getCourseId();
        }
    }

    @AfterEach
    void tearDown() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            if (student != null) {
                Course course = session.get(Course.class, courseId);
                if (course != null) {
                    student.removeCourse(course);
                }
                session.remove(student);
            }
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                session.remove(course);
            }

            tx.commit();
        }
    }

    @Test
    void coursesCollectionIsNotInitializedImmediatelyAfterFetchingStudent() {
        try (Session session = sessionFactory.openSession()) {
            Student student = session.get(Student.class, studentId);
            assertNotNull(student);
            assertFalse(Hibernate.isInitialized(student.getCourses()),
                    "The lazy 'courses' collection must NOT be initialized just from loading the Student");
        }
    }

    @Test
    void accessingLazyCollectionAfterSessionClosesThrowsLazyInitializationException() {
        Student detachedStudent;

        try (Session session = sessionFactory.openSession()) {
            detachedStudent = session.get(Student.class, studentId);
        }
        // Session is now closed - the "courses" proxy collection is detached.

        assertThrows(LazyInitializationException.class, () -> detachedStudent.getCourses().size(),
                "Accessing an uninitialized lazy collection after the Session closes must throw LazyInitializationException");
    }

    @Test
    void accessingLazyCollectionWithinOpenSessionWorksCorrectly() {
        try (Session session = sessionFactory.openSession()) {
            Student student = session.get(Student.class, studentId);

            assertFalse(Hibernate.isInitialized(student.getCourses()));

            // Explicit initialization while the Session is still open succeeds.
            Hibernate.initialize(student.getCourses());

            assertTrue(Hibernate.isInitialized(student.getCourses()));
            assertEquals(1, student.getCourses().size());
        }
    }

    @Test
    void joinFetchHqlEagerlyLoadsCoursesWithinSession() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select distinct s from Student s left join fetch s.courses where s.studentId = :id";
            Student student = session.createQuery(hql, Student.class)
                    .setParameter("id", studentId)
                    .uniqueResult();

            assertNotNull(student);
            assertTrue(Hibernate.isInitialized(student.getCourses()));
            assertEquals(1, student.getCourses().size());
        }
    }
}
