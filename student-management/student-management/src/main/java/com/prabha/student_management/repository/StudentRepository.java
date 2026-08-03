package com.prabha.student_management.repository;

import com.prabha.student_management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Student}.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /** Find all students belonging to the given department. */
    List<Student> findByDepartment(String department);

    /** Find all students whose CGPA is strictly greater than the given value. */
    List<Student> findByCgpaGreaterThan(BigDecimal cgpa);

    /** Look up a student by their (unique) email address. */
    Optional<Student> findByEmail(String email);

    /** Check whether a student with the given email already exists. */
    boolean existsByEmail(String email);
}
