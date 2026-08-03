package com.prabha.student_course_registration.dao;

import com.prabha.student_course_registration.entity.Course;

import java.util.Optional;

/**
 * Data-access contract for {@link Course}. Implementations use plain
 * Hibernate (SessionFactory / Session / Transaction / HQL) directly -
 * no Spring Data JPA repositories are involved.
 */
public interface CourseDao {

    Course save(Course course);

    Optional<Course> findById(Long courseId);

    /**
     * Loads a course together with its "students" collection initialized
     * (via an HQL "join fetch"), all within a single Session, so the result
     * can be safely read after the Session closes.
     */
    Optional<Course> findByIdWithStudents(Long courseId);

    Optional<Course> findByCourseCode(String courseCode);
}
