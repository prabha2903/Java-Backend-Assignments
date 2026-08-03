package com.prabha.student_course_registration.dao;

/**
 * Data-access contract for managing the Student <-> Course enrollment
 * (the "student_course" join table). Kept separate from StudentDao/CourseDao
 * since it operates on both aggregates atomically within a single
 * Hibernate Session/Transaction.
 */
public interface EnrollmentDao {

    /**
     * Enrolls the given student into the given course.
     *
     * @throws com.prabha.student_course_registration.exception.StudentNotFoundException   if the student does not exist
     * @throws com.prabha.student_course_registration.exception.CourseNotFoundException     if the course does not exist
     * @throws com.prabha.student_course_registration.exception.DuplicateEnrollmentException if the student is already enrolled in the course
     */
    void enroll(Long studentId, Long courseId);

    /**
     * Removes an existing enrollment of the given student from the given course.
     *
     * @throws com.prabha.student_course_registration.exception.StudentNotFoundException    if the student does not exist
     * @throws com.prabha.student_course_registration.exception.CourseNotFoundException      if the course does not exist
     * @throws com.prabha.student_course_registration.exception.EnrollmentNotFoundException if no such enrollment exists
     */
    void unenroll(Long studentId, Long courseId);
}
