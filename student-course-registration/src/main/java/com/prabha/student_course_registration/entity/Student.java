package com.prabha.student_course_registration.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Student entity.
 *
 * Owning side of the Student <-> Course Many-to-Many relationship. The
 * relationship is backed by a dedicated join table ("student_course") and is
 * configured with FetchType.LAZY so the "courses" collection is only loaded
 * from the database when it is explicitly accessed/initialized.
 */
@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "student_name", nullable = false, length = 150)
    private String studentName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    public Student(String studentName, String email, String department) {
        this.studentName = studentName;
        this.email = email;
        this.department = department;
    }

    /**
     * Maintains both sides of the bidirectional association in memory.
     * The join table row is driven by this (the owning) side.
     */
    public void addCourse(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);
    }

    /**
     * Removes both sides of the bidirectional association in memory.
     */
    public void removeCourse(Course course) {
        this.courses.remove(course);
        course.getStudents().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        Student student = (Student) o;
        return studentId != null && studentId.equals(student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }
}
