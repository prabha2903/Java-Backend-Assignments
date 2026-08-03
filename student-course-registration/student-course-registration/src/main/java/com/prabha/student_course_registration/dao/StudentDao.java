package com.prabha.student_course_registration.dao;

import com.prabha.student_course_registration.entity.Student;

import java.util.Optional;

/**
 * Data-access contract for {@link Student}. Implementations use plain
 * Hibernate (SessionFactory / Session / Transaction / HQL) directly -
 * no Spring Data JPA repositories are involved.
 */
public interface StudentDao {

    Student save(Student student);

    Optional<Student> findById(Long studentId);

    /**
     * Loads a student together with its "courses" collection initialized
     * (via an HQL "join fetch"), all within a single Session, so the result
     * can be safely read after the Session closes.
     */
    Optional<Student> findByIdWithCourses(Long studentId);

    Optional<Student> findByEmail(String email);
}
