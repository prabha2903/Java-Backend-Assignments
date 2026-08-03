package com.prabha.student_course_registration.service;

import com.prabha.student_course_registration.dao.EnrollmentDao;
import org.springframework.stereotype.Service;

/**
 * Business logic for enrolling/unenrolling students from courses.
 */
@Service
public class EnrollmentService {

    private final EnrollmentDao enrollmentDao;

    public EnrollmentService(EnrollmentDao enrollmentDao) {
        this.enrollmentDao = enrollmentDao;
    }

    public void enrollStudentInCourse(Long studentId, Long courseId) {
        enrollmentDao.enroll(studentId, courseId);
    }

    public void removeEnrollment(Long studentId, Long courseId) {
        enrollmentDao.unenroll(studentId, courseId);
    }
}
