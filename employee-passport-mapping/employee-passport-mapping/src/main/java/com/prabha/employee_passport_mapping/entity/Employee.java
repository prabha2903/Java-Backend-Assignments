package com.prabha.employee_passport_mapping.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Employee entity - the inverse (non-owning) side of the Employee <-> Passport
 * one-to-one association.
 *
 * fetch = FetchType.LAZY on purpose: loading an Employee should NOT
 * automatically trigger a second SELECT for its Passport unless it is
 * explicitly requested (see EmployeeDaoImpl#findByIdWithPassport which uses
 * a HQL "join fetch" to eagerly pull the Passport in a single query).
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "passport")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    @EqualsAndHashCode.Include
    private Long employeeId;

    @Column(name = "employee_name", nullable = false, length = 150)
    private String employeeName;

    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @Column(name = "salary", nullable = false)
    private Double salary;

    /**
     * Inverse side. Passport owns the foreign key (employee_id column on the
     * passports table). CascadeType.ALL + orphanRemoval = true means:
     *  - saving/updating an Employee cascades to its Passport
     *  - deleting an Employee automatically deletes its associated Passport
     *  - unlinking a Passport from an Employee deletes the orphaned Passport
     */
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Passport passport;

    public void assignPassport(Passport passport) {
        this.passport = passport;
        if (passport != null) {
            passport.setEmployee(this);
        }
    }
}
